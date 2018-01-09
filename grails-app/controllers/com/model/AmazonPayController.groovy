package com.model

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import traderapp.model.Customer
import traderapp.model.FacebookUser
import traderapp.model.Invoice
import traderapp.model.Jobs
import traderapp.model.LinkedInUser
import traderapp.model.Tradesman
import traderapp.user.UserRole

import com.amazon.pay.Client
import com.amazon.pay.Config
import com.amazon.pay.impl.PayClient
import com.amazon.pay.impl.PayConfig
import com.amazon.pay.request.AuthorizeRequest
import com.amazon.pay.request.CloseOrderReferenceRequest
import com.amazon.pay.request.ConfirmOrderReferenceRequest
import com.amazon.pay.request.SetOrderReferenceDetailsRequest
import com.amazon.pay.response.parser.AuthorizeResponseData
import com.amazon.pay.response.parser.ConfirmOrderReferenceResponseData
import com.amazon.pay.response.parser.SetOrderReferenceDetailsResponseData
import com.amazon.pay.types.CurrencyCode
import com.amazon.pay.types.Region
import com.amazon.pay.types.User

/**
 * Amazon Pay - Payment API
 * APIs integrated - SetOrderRefDetailsRequest,
 * AuthorizeRequest, Capture Payment, GetUserProfile
 */
@Slf4j
class AmazonPayController {
	
	def springSecurityService;
	
	/**
	 * When AmazonPay throws an error, this method is visited 
	 * which redirects to corresponding user's error page
	 */
	def error(){
		String errorMsg = params.errorAPI;
		log.info "error() | Amazon Pay API threw an error : " + errorMsg;
		if(errorMsg){
			if(errorMsg == "AlreadyPaid"){
				redirect controller:"profile", action:"paid", params:[invoiceId:params.invoiceId];
			} else{
				if(params.invoiceId){
					redirect controller:"profile", action:"error", params:[invoiceId:params.invoiceId];
				} else{
					redirect controller:"profile", action:"error", params:[invoiceId:null];
				}
			}
		}
	}
	
	
	/**
	 * Redirects to Wallet Widget Page with 
	 * Buyer's public info
	 * User AmamzonPay - getUserInfo API
	 */
	def pay(){
		log.info "pay() | Method redirects to Wallet widget of Amazon Pay Buyer"
		if(params.invoice){
			log.info "params contains invoice id : " + params.invoice;
			Invoice invoice = Invoice.get(params.invoice?.toLong());
			if(invoice){
				log.info "Invoice Instance found with id $params.invoice";
				Jobs job = Jobs.findByInvoice(invoice);
				if(job){
					log.info "Job Instance found for Invoice " + invoice;
					Tradesman tradesman = job?.tradesman;
					log.info "Tradesman Instance found " + tradesman;
					if(tradesman?.amazon_merchantId && tradesman?.amazon_clientId && tradesman?.amazon_accessKey && tradesman?.amazon_secretKey){
						log.info "Tradesman has all necessary credentials for Amazon Pay"
						if(params.access_token){
							log.info "Params contains access token for Amazon Pay API"
							String merchantId = tradesman?.amazon_merchantId; //"A36CCH24SKY06R";
							String accessKey =  tradesman?.amazon_accessKey; //"AKIAJQWGJU5TMDKRTHKA";
							String secretKey =  tradesman?.amazon_secretKey; //"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";
							String clientId =  tradesman?.amazon_clientId; //"amzn1.application-oa2-client.3f6b64447b764f6fb3e438e0d154d78a";
							
							Config config = new PayConfig()
											.withSellerId(merchantId)
											.withAccessKey(accessKey)
											.withSecretKey(secretKey)
											.withCurrencyCode(CurrencyCode.GBP)
											.withSandboxMode(true)
											.withRegion(Region.UK);
							Client client = new PayClient(config);
							log.info "Config and Client for AmazonPay SDK are created"
							try{
								log.info "Getting buyer's info using GetUserInfo API";
								User user = client.getUserInfo(params.access_token, clientId);
								if(user){
									traderapp.user.User loggedInUser = springSecurityService.currentUser;
									UserRole userRole = UserRole.findByUser(loggedInUser);
									log.info "Redirecting to corresponding user's wallet widget"
									if(userRole?.role?.authority == "ROLE_CUSTOMER"){
										Customer customerInstance = Customer.findByEmail(invoice?.customerEmail);
										[buyerName:user.getName(), buyerEmail:user.getEmail(), userId:user.getUserId(), invoice:invoice, tradesman:tradesman, customer:customerInstance]
									} else if(userRole?.role?.authority == "ROLE_FACEBOOK_CUSTOMER"){
										FacebookUser fbUser = FacebookUser.findByEmail(invoice?.customerEmail);
										[buyerName:user.getName(), buyerEmail:user.getEmail(), userId:user.getUserId(), invoice:invoice, tradesman:tradesman, customer:fbUser]
									} else if(userRole?.role?.authority == "ROLE_LINKEDIN_CUSTOMER"){
										LinkedInUser liUser = LinkedInUser.findByEmail(invoice?.customerEmail);
										[buyerName:user.getName(), buyerEmail:user.getEmail(), userId:user.getUserId(), invoice:invoice, tradesman:tradesman, customer:liUser]
									}
								}
							} catch(Exception e){
								log.info "Error while fetching user profile of buyer : " + e.message;
								e.printStackTrace();
								redirect action:'error', params:[errorAPI:"UserProfile", invoiceId:invoice?.id];
							}
						} else{
							log.info "No Access Token received after login. Aborting current transaction."
							redirect action:'error', params:[errorAPI:"No Access Token", invoiceId:invoice?.id];
						}
					} else{
						log.info "Tradesman does not have all amazon pay credentials. Aborting current transaction."
						redirect action:'error', params:[errorAPI:"No AmazonPay Credentials", invoiceId:invoice?.id];
					}
				} else{
					log.info "No Job found associated with this invoice. Aborting transaction.";
					redirect action :'error', params:[errorAPI:'No Job', invoiceId:invoice?.id]
				}
			} else{
				log.info "No Invoice found. Aborting transaction.";
				redirect action :'error', params:[errorAPI:'No Invoice', invoiceId:null]
			}
		} else{
			log.info "No Invoice Id Received in parameters";
			redirect action :'error', params:[errorAPI:'No Invoice', invoiceId:null]
		}
	}
	
	/**
	 * AmazonPay successful transaction redirects here.
	 * And this method redirects to corresponding user's success page
	 */
	def success(){
		log.info "success() | params : $params";
		if(params.invoice && params.tradesman && params.amazonOrderRefId){
			Invoice invoice = Invoice.get(params.invoice?.toLong());
			Tradesman tradesman = Tradesman.get(params.tradesman?.toLong());
			Jobs job =  Jobs.findByInvoice(invoice);
			log.info "Invoice, Tradesman and Jobs Instances found from params : " + invoice + " " + tradesman + " " + job;
			invoice.setStatus("PAID");
			invoice.setTransactionID(params.amazonOrderRefId);
			invoice.setPaymentType("AMAZON PAY")
			invoice.save(flush:true);
			
			if(job){
				job.setStatus("PAID");
				job.setInvoiceStatus("INVOICE_PAID");
				job.save(flush:true);
			}
			log.info "Updated payment status and invoice status in Invoice and Jobs instances";
			redirect controller:"profile", action:"success", params:[invoiceId:invoice?.id];
		}
	}
	
	/**
	 * When buyer cancels payment procedure, this method redirects to cancel page
	 * @param id - InvoiceInstance Id
	 */
	def cancel(Long id){
		log.info "cancel() | Invoice id to cancel payment : " + id;
		if(id){
			Invoice invoice = Invoice.get(id);
			redirect controller:"profile", action:"cancel", params:[invoiceId:invoice?.id];
		}
	}
	
	/**
	 * AmazonPay Client sets order reference details request,
	 * confirms the same order reference details request and authorizes the same
	 * @return Amazon OrderRefId
	 */
	def approve(){
		log.info "approve() | params : " + params;
		if(params){
			if(params.tradesmanId){
				log.info "Params have tradesman id. Let's find tradesman instance"
				Tradesman tradesman = Tradesman.get(params.tradesmanId?.toLong());
				Invoice invoice = Invoice.get(params.invoiceId);
				if(invoice?.status == "PAID"){
					log.info("Invoice  is already paid");
					redirect action:'error', params:[errorAPI:"AlreadyPaid", invoiceId:invoice?.id];
				} else{
					if(tradesman && invoice){
						log.info "Tradesman and Invoice instance found : " + tradesman + " " + invoice;
						if(tradesman?.amazon_merchantId && tradesman?.amazon_clientId && tradesman?.amazon_accessKey && tradesman?.amazon_secretKey){
							log.info "Tradesman has all necessary credentials for Amazon Pay"
							try{
								String merchantId = tradesman?.amazon_merchantId; //"A36CCH24SKY06R";
								String accessKey =  tradesman?.amazon_accessKey; //"AKIAJQWGJU5TMDKRTHKA";
								String secretKey =  tradesman?.amazon_secretKey; //"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";
								String clientId =  tradesman?.amazon_clientId; //"amzn1.application-oa2-client.3f6b64447b764f6fb3e438e0d154d78a";
								
								Config config = new PayConfig()
												.withSellerId(merchantId)
												.withAccessKey(accessKey)
												.withSecretKey(secretKey)
												.withCurrencyCode(CurrencyCode.GBP)
												.withSandboxMode(true)
												.withRegion(Region.UK);
								Client client = new PayClient(config);
								
								log.info "Config and Client for AmazonPay SDK are created"
								String invoiceNumber = invoice?.invoiceNumber;
								String orderRefId = params.orderRefId;
								String amount = params.amount;
								try{
									log.info "Creating SetOrderReferenceDetailsRequest"
									SetOrderReferenceDetailsRequest setOrderRefDetails = new SetOrderReferenceDetailsRequest(orderRefId, amount);
									setOrderRefDetails.setOrderCurrencyCode(CurrencyCode.GBP);
									setOrderRefDetails.setSellerOrderId(invoiceNumber);
									SetOrderReferenceDetailsResponseData setOrderRef_resp =  client.setOrderReferenceDetails(setOrderRefDetails);
									log.info "SetOrderReferenceDetailsRequest response code : " + setOrderRef_resp?.statusCode;
									if(setOrderRef_resp?.statusCode == 200){
										try{
											log.info "Creating ConfirmOrderReferenceRequest"
											ConfirmOrderReferenceRequest confirmOrderReq = new ConfirmOrderReferenceRequest(orderRefId);
											ConfirmOrderReferenceResponseData confirmOrderReq_resp = client.confirmOrderReference(confirmOrderReq);
											log.info "ConfirmOrderReferenceRequest response code : " + confirmOrderReq_resp?.statusCode;
											if(confirmOrderReq_resp?.statusCode == 200){
												try{
													log.info "Creating AuthorizeAndCaptureRequest";
													AuthorizeRequest auth_req = new AuthorizeRequest(orderRefId, new Date().getTime()?.toString(), amount);
													auth_req.setAuthorizationCurrencyCode(CurrencyCode.GBP);
													auth_req.setTransactionTimeout("0");
													auth_req.setCaptureNow(true);
													AuthorizeResponseData auth_response = client.authorize(auth_req);
													def slurper = new JsonSlurper();
													def result = slurper.parseText(auth_response?.toJSON()?.toString());
													def transactionStatus = result?.AuthorizeResponse?.AuthorizeResult?.AuthorizationDetails?.AuthorizationStatus?.State;
													log.info "AuthorizeRequest response code : " + transactionStatus
													if(auth_response?.statusCode == 200 && transactionStatus == "Closed"){
														log.info "Closing Order Reference"
														client.closeOrderReference(new CloseOrderReferenceRequest(orderRefId));
														redirect action:'success', params:[invoice:invoice.id,  tradesman:tradesman.id, amazonOrderRefId:orderRefId];
													} else{
														log.info "AuthorizeRequest Status code other than 200 : " + auth_response?.statusCode;
														redirect action:'error', params:[errorAPI:"AuthorizeRequest ErrorStatusCode", invoiceId:invoice?.id];
													}
												} catch(Exception e4){
													log.info "Error in AuthorizeRequest : " + e4.message;
													e4.printStackTrace();
													redirect action:'error', params:[errorAPI:"AuthorizeRequest", invoiceId:invoice?.id];
												}
											} else{
												log.info "ConfirmOrderReferenceRequest Status code other than 200 : " + confirmOrderReq_resp?.statusCode;
												redirect action:'error', params:[errorAPI:"ConfirmOrderReferenceRequest ErrorStatusCode", invoiceId:invoice?.id];
											}
										} catch(Exception e3){
											log.info "Error in ConfirmOrderReferenceRequest : " + e3.message;
											e3.printStackTrace();
											redirect action:'error', params:[errorAPI:"ConfirmOrderReferenceRequest", invoiceId:invoice?.id];
										}
									} else{
										log.info "SetOrderReferenceDetailsRequest Status code other than 200 : " + setOrderRef_resp?.statusCode;
										redirect action:'error', params:[errorAPI:"SetOrderReferenceDetailsRequest ErrorStatusCode", invoiceId:invoice?.id];
									}
								} catch(Exception e2){
									log.info "Error in SetOrderReferenceDetailsRequest : " + e2.message;
									e2.printStackTrace();
									redirect action:'error', params:[errorAPI:"SetOrderReferenceDetailsRequest", invoiceId:invoice?.id];
								}
							} catch(Exception e1){
								log.info "Error in Amazon Pay API : " + e1.message;
								e1.printStackTrace();
								redirect action:'error', params:[errorAPI:"Amazon Pay", invoiceId:invoice?.id];
							}
						} else{
							log.info "Tradesman does not have all amazon pay credentials. Aborting current transaction."
							redirect action:'error', params:[errorAPI:"No AmazonPay Credentials", invoiceId:invoice?.id];
						}
					} else{
						log.info "No tradesman or Invoice found associated with this transaction. Aborting current transaction.";
						redirect action:'error', params:[errorAPI:"No Tradesman or Invoice", invoiceId:null];
					}
				}
			} else{
				log.info "No tradesman found associated with this transaction. Aborting current transaction";
				redirect action:'error', params:[errorAPI:"No Tradesman", invoiceId:null];
			}
		} else{
			log.info "No parameters received for billing to proceed.";
			redirect action:'error', params:[errorAPI:"No Parameters", invoiceId:null];
		}
	}
}
