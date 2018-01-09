package com.model

import java.lang.invoke.InfoFromMemberName

import grails.util.Environment
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import org.springframework.beans.factory.annotation.Value

import traderapp.model.Invoice
import traderapp.model.Jobs
import traderapp.model.Tradesman

import com.paypal.base.*

/**
 * Paypal - Payment API
 */
@Slf4j
class PaypalController {
	def paypalService;
	
	@Value('${paypal.endpoint}')
	String paypal_endpoint;
	
	@Value('${paypal.returnUrl}')
	String paypal_returnUrl;
	
	@Value('${paypal.cancelUrl}')
	String paypal_cancelUrl;
	
	/**
	 * Approves tradesman's seller credentials for PayPal
	 * and redirects buyer to PayPal Buyer Login
	 * Sets Success and Cancel URL for transaction
	 */
	def approve(){
			log.info "approve() | params : $params";
			if(params.invoiceId){
				log.info "params has invoice id : $params.invoiceId";
				Invoice invoice = Invoice.get(params.invoiceId?.toLong());
				if(invoice && (invoice?.status == "SENT" || invoice?.status == "OVERDUE")){
					log.info "Invoice instance found and it has status PENDING"
					Map sdkConfig = [:]
					Tradesman tradesman = Tradesman.findByCompanyEmail(invoice?.tradesmanEmail);
					if(tradesman?.paypal_clientId && tradesman?.paypal_clientSecret){
						log.info "Tradesman had all necessary credentials for PayPal payments"
						try{
							sdkConfig.put("clientId",tradesman?.paypal_clientId);
							sdkConfig.put("clientSecret",tradesman?.paypal_clientSecret)
							sdkConfig.put("service.EndPoint",paypal_endpoint)
							def accessToken = paypalService.getAccessToken(tradesman?.paypal_clientId,tradesman?.paypal_clientSecret,sdkConfig)
							def apiContext = paypalService.getAPIContext(accessToken,sdkConfig)
							def amount = paypalService.createAmount(['currency':"GBP",'total':invoice?.totalPrice?.toString()])
							def transaction = paypalService.createTransaction(['amount':amount,'description':invoice?.invoiceNumber])
							def transactions = []
							transactions.add(transaction)
							def payer = paypalService.createPayer(['paymentMethod':'paypal'])
							def cancelUrl=paypal_cancelUrl+invoice?.invoiceNumber;
							def returnUrl = paypal_returnUrl+invoice?.invoiceNumber;
							def redirectUrls = paypalService.createRedirectUrls(['cancelUrl':cancelUrl,'returnUrl':returnUrl])
							def payment
							try{
								log.info "Creating PayPal Payment"
								
								payment = paypalService.createPayment(['payer':payer,'intent':'sale'
										,'transactionList':transactions,'redirectUrls':redirectUrls
										,'apiContext':apiContext])
							} catch(Exception ex){
								log.info "Errr occurred during approving transaction : " + ex.message;
								String msg = ex.getMessage()
								flash.message = "Could not complete the transaction because: ${msg? msg : ''}"
								redirect controller:'paypal', action:"error"
								return
							}
							def approvalUrl = ""
							def retUrl = ""
							// retrieve links from returned paypal object
							payment?.links.each{
								if(it?.rel == 'approval_url'){
									approvalUrl = it.href
								}
								if(it?.rel == 'return_url'){
									retUrl = it.href
								}
							}
							log.info "Redirecting to approve URL"
							redirect url:approvalUrl? approvalUrl:'/', method:'POST'
						} catch(Exception e){
							e.printStackTrace();
							log.info "Redirecting to error URL : " + e.message;
							redirect controller:'paypal', action:"error", params:[invoiceID:invoice?.invoiceNumber]
						}
					} else {
						log.info "Redirecting to error URL : Tradesman does not have PayPal credentials"
						redirect controller:'paypal', action:"error", params:[invoiceID:invoice?.invoiceNumber]
					}
				}
			}
		}
		
		/**
		 * @param invoiceID - Invoice number
		 * Executes PayPal Payment after selection of shipping address and Payment method
		 */
		def execute(String invoiceID){
			Invoice invoice = Invoice.findByInvoiceNumber(invoiceID);
			Tradesman tradesman = Tradesman.findByCompanyEmail(invoice?.tradesmanEmail);
			log.info "execute() | Invoice and Tradesman instance found : " + invoice + " " + tradesman;
			try{
				Map sdkConfig = [:] //= grailsApplication.config.paypal.sdkConfig//['mode':'live']
				//sdkConfig.put("grant-type","client_credentials")
				sdkConfig.put("clientId",tradesman?.paypal_clientId)
				sdkConfig.put("clientSecret",tradesman?.paypal_clientSecret)
				sdkConfig.put("service.EndPoint",paypal_endpoint)
				def accessToken = paypalService.getAccessToken(tradesman.paypal_clientId,tradesman?.paypal_clientSecret,sdkConfig)
				def apiContext = paypalService.getAPIContext(accessToken,sdkConfig)
				//the paypal website will add params to the call to your app. Eg. PayerId, PaymentId
				// you will use the params to 'execute' the payment
				def paypalPayment = paypalService.createPaymentExecution(['paymentId':params.paymentId,'payerId':params?.PayerID],apiContext)
			
				JsonSlurper slurper = new JsonSlurper()
				def map = slurper.parseText(paypalPayment.toString())
				log.info "PayPal Payment Response : " + map;
				String transactionId = map.transactions[0].related_resources[0].sale.id;
				
				if(invoice){
					invoice.setStatus("PAID");
					invoice.setTransactionID(transactionId);
					invoice.setPaymentType("PAYPAL")
					invoice.save(flush:true);
					
					Jobs job = Jobs.findByInvoice(invoice);
					if(job){
						job.setStatus("PAID");
						job.setInvoiceStatus("INVOICE_PAID");
						job.save(flush:true);
					}
				}
				
				log.info "Invoice and Jobs Instance payment status updated"
				redirect controller:"profile", action:"success", params:[invoiceId:invoice?.id];
			} catch(Exception e){
				log.info "Error while executing PayPal Payment : " + e.message;
				redirect controller:'paypal', action:"error", params:[invoiceID:invoice?.invoiceNumber]
			}
		}
		
		/**
		 * @param invoiceID - Invoice Number to cancel PayPal transaction
		 */
		def cancel(String invoiceID){
			log.info "cancel() | params : $params";
			Invoice invoice = Invoice.findByInvoiceNumber(invoiceID);
			redirect controller:"profile", action:"cancel", params:[invoiceId:invoice?.id];
		}
		
		/**
		 * @param invoiceID - Invoice Number to PayPal transaction which failed
		 * Redirects to Error page of failed PayPal Transaction
		 * @return
		 */
		def error(String invoiceID){
			log.info "error() | params : $params";
			Invoice invoice = Invoice.findByInvoiceNumber(invoiceID);
			redirect controller:"profile", action:"error", params:[invoiceId:invoice?.id];
		}
}
