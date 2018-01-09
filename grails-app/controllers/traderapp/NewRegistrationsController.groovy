package traderapp

import grails.gsp.PageRenderer
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegistrationCode
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

import traderapp.model.Customer
import traderapp.model.Emails
import traderapp.model.FacebookUser
import traderapp.model.Jobs
import traderapp.model.LinkedInUser
import traderapp.model.Tradesman
import traderapp.user.Role
import traderapp.user.User
import traderapp.user.UserRole

/**
 * Handles Tradesman and Customer Registration
 * Facebook and LinkedIn Login
 * Renders logo in Email and determines if email was open or not
 */
@Slf4j
class NewRegistrationsController {
	def mailService;
	def springSecurityService;
	PageRenderer groovyPageRenderer;
	def assetResourceLocator;
	def springSecurityUiService;
	def saltSource;
	
	/**
	 * Redirects to Customer registration form 
	 * with / without parameters
	 */
	def createNewCustomer(){
		log.info "createNewCustomer() | params for customer registration : $params";
		String contactName = params.contactName;
		String contactEmail = params.contactEmail;
		String invoiceID = params.invoiceID;
		Emails email = Emails.findByCustomerEmailAndInvoiceNumber(contactEmail, invoiceID);
		if(email){
			if(email?.emailOneSent){
				if(email?.emailTwoSent){
					if(email?.followUpMailSent){
						email.setFollowUpMailOpen(true);
						email.setRegistrationLinkClickedFollowUpMail(true);
						email.save(flush:true);
					} else{
						email?.setEmailTwoOpen(true);
						email?.setRegistrationLinkClickedSecondMail(true);
						email.save(flush:true);
					}
				} else{
					email.setEmailOneOpen(true);
					email.setRegistrationLinkClickedFirstMail(true);
					email.save(flush:true);
				}
			}
		}
		[contactName: contactName, contactEmail:contactEmail, invoiceID:invoiceID]
	}
	
	/**
	 * Redirects to Tradesman registration form 
	 * with / without parameters
	 */
	def createNewTradesman(){
		log.info "createNewTradesman() | params for tradesman registration : $params";
		String contactName = params.contactName;
		String companyName = params.companyName;
		String companyEmail = params.companyEmail;
		String jobId = params.jobId;
		if(jobId){
			Jobs job = Jobs.get(jobId?.toLong());
			if(job?.jobCreationMailSent_one){
				if(job?.jobCreationMailSent_two){
					if(job?.jobCreationMailSent_three){
						job?.setJobCreationMail_three_read(true);
						job?.setRegistrationLinkClicked(true);
						job.save(flush:true);
					} else{
						job?.setJobCreationMail_two_read(true);
						job?.setRegistrationLinkClicked(true);
						job.save(flush:true);
					}
				} else{
					job?.setJobCreationMail_one_read(true);
					job?.setRegistrationLinkClicked(true);
					job.save(flush:true);
				}
			}
		}
		
		[contactName:contactName, companyName:companyName, companyEmail:companyEmail, jobId:jobId]
	}
	
	/**
	 * Create and save new tradesman instance
	 * Send confirmation email to tradesman
	 */
	def saveNewTradesman(){
		log.info "saveNewTradesman() | params from new registration of tradesman form : $params";
		if(params.name?.trim() && params.companyName?.trim() && params.email?.trim() && params.password?.trim() && params.reEnteredPassword?.trim()){
			if(params.password?.trim() == params.reEnteredPassword?.trim()){
				def user = User.findByUsername(params.email);
				if(!user){
					try{
						user = new User(username:params.email, password:params.password, enabled:true).save(flush:true);
						def user_role = new UserRole(user:user, role:Role.findByAuthority('ROLE_TRADESMAN')).save(flush:true);
						def tradesman = new Tradesman();
						tradesman.setName(params.name);
						tradesman.setCompanyName(params.companyName);
						tradesman.setCompanyEmail(params.email);
						tradesman.setUser(user);
						tradesman.save(flush:true);
						//tradesman.workType = ["Test1","Test2"];
						tradesman.save(flush:true);
						if(params.workType_Electrical == "on"){
							log.info "Type : Electrical";
							tradesman.addToWorkType("Electrical").save(fluh:true);
						}
						if(params.workType_Plumbing == "on"){
							log.info "Type : Plumbing";
							tradesman.addToWorkType("Plumbing").save(fluh:true);
						}
						if(params.workType_Gas == "on"){
							log.info "Type : Gas";
							tradesman.addToWorkType("Gas").save(fluh:true);
						}
						if(params.workType_Plastering == "on"){
							log.info "Type : Plastering";
							tradesman.addToWorkType("Plastering").save(fluh:true);
						}
						if(params.workType_Tiling == "on"){
							log.info "Type : Tiling";
							tradesman.addToWorkType("Tiling").save(fluh:true);
						}
						if(params.workType_Painting_Decorating == "on"){
							log.info "Type : Painting / Decorating";
							tradesman.addToWorkType("Painting / Decorating").save(fluh:true);
						}
						
						tradesman.save(flush:true);
						log.info "Worktypes in saved instance : " + tradesman.workType;
						log.info "Tradesman instance after saving work types : " + tradesman;
						log.info "Sending account creation confirmation mail to tradesman"
						def emailSent = sendConfirmationMail("TRADESMAN", params.name,params.email,params.companyName);
						if(emailSent == true){
							springSecurityService.reauthenticate user.username
							redirect controller:"home", action:"index"
						} else{
							log.info "Error while sending confirmation email"
							flash.message="Error while sending confirmation email !!";
							chain action:"createNewTradesman", params:[contactName:params.name, companyName:params.companyName, companyEmail:params.email]
						}
						
						def jobs = Jobs.findAllByTradesmanEmail(tradesman?.companyEmail);
						if(jobs){
							log.info "Jobs are already created by customer for newly created tradesman"
							for(Jobs job:jobs){
								job.setTradesman(tradesman);
								job.save(flush:true);
							}
						}						
					} catch(Exception e){
						log.info "Error while saving tradesman instance : " + e.message;
						e.printStackTrace();
						flash.message = "Error while saving tradesman instance !!"
						chain action:"createNewTradesman", params:[contactName:params.name, companyName:params.companyName, companyEmail:params.email]
					}
				} else{
					log.info "User exists with same email address"
					flash.message = "User exists with same email address !!"
					chain action:"createNewTradesman", params:[contactName:params.name, companyName:params.companyName, companyEmail:params.email]
				}
			} else{
				log.info "Password and re-entered password should be same"
				flash.message = "Password and re-entered password should be same !!"
				chain action:"createNewTradesman", params:[contactName:params.name, companyName:params.companyName, companyEmail:params.email]
			}
		} else{
			log.info "Please Enter valid credentials"
			flash.message = "Please Enter valid credentials !!"
			chain action:"createNewTradesman", params:[contactName:params.name, companyName:params.companyName, companyEmail:params.email]
		}		
	}
	
	/**
	 * Create and save new customer instance
	 * Send confirmation email to customer
	 */
	def saveNewCustomer(){
		log.info "saveNewCustomer() | params from new registration of customer form : $params";
		if(params.name?.trim() && params.email?.trim() && params.password?.trim() && params.reEnteredPassword?.trim()){
			if(params.password?.trim() == params.reEnteredPassword?.trim()){
				def user = User.findByUsername(params.email);
				if(!user){
					try{
						user = new User(username:params.email, password:params.password, enabled:true).save(flush:true);
						def user_role = new UserRole(user:user, role:Role.findByAuthority('ROLE_CUSTOMER')).save(flush:true);
						def customer = new Customer();
						customer.setName(params.name);
						customer.setEmail(params.email);
						customer.setUser(user);
						customer.save(fluh:true);
						log.info "Customer instance created"
						log.info "Sending account creation confirmation mail to customer"
						def emailSent = sendConfirmationMail("CUSTOMER", params.name, params.email, null);
						if(emailSent == true){
							log.info "Confirmation Email sent successfully"
							Emails email = Emails.findByCustomerEmailAndInvoiceNumber(params.email, params.invoiceID);
							if(email){
								email.setCustomerRegistered(true);
								email.save(flush:true);
								def notifyTradesman = sendNotificationEmailToTradesman(customer?.name, customer?.email, email?.tradesmanEmail, "CustomerRegistration");
								if(notifyTradesman == true){
									springSecurityService.reauthenticate user.username
									redirect controller:"home", action:"index"
								} else{
									flash.message = "Error while sending notification email to Tradesman !!"
									springSecurityService.reauthenticate user.username
									redirect controller:"home", action:"index"
								}
							} else{
								springSecurityService.reauthenticate user.username
								redirect controller:"home", action:"index"
							}
						} else{
							log.info "Error while sending confirmation email"
							flash.message = "Error while sending confirmation email !!"
							chain action:"createNewCustomer", params:[contactName:params.name, contactEmail:params.email]
						}
					} catch(Exception e){
						log.info "Error while saving customer instance : " + e.message;
						e.printStackTrace();
						flash.message = "Error while saving customer instance !!"
						chain action:"createNewCustomer", params:[contactName:params.name, contactEmail:params.email]
					}
				} else{
					Emails email = Emails.findByCustomerEmailAndInvoiceNumber(params.email, params.invoiceID);
					if(email){
						email.setCustomerRegistered(true);
						email.save(flush:true);
						Customer customer = Customer.findByEmail(params.email);
						def notifyTradesman = sendNotificationEmailToTradesman(customer?.name, customer?.email, email?.tradesmanEmail, "CustomerRegistration");
					}
					log.info "User already exists with same email address"
					flash.message = "User already exists with same email address !!"
					chain action:"createNewCustomer", params:[contactName:params.name, contactEmail:params.email]
				}
			} else{
				log.info "Password and re-entered password should be same"
				flash.message = "Password and re-entered password should be same !!"
				chain action:"createNewCustomer", params:[contactName:params.name, contactEmail:params.email]
			}
		} else{
			log.info "Please Enter valid credentials"
			flash.message = "Please Enter valid credentials !!"
			chain action:"createNewCustomer", params:[contactName:params.name, contactEmail:params.email]
		}
	}
	
	/**
	 * Notifies tradesman about customer registration 
	 * for whom he has sent invoice and account creation request on TradeRefer
	 * @param customer - Customer Instance
	 * @param tradesmanEmail - Tradesman Email Address
	 * @param notificationType - CustomerRegistration
	 * @return boolean
	 */
	def sendNotificationEmailToTradesman(String customerName, String customerEmail, String tradesmanEmail, String notificationType){
		log.info "sendNotificationEmailToTradesman()"
		if(notificationType.equalsIgnoreCase("CustomerRegistration")){
			try{
				mailService.sendMail{
					to customer?.email
					subject "Customer Registration Completed -" + customerName + "(" + customerEmail +")"
					body "Customer "+customerName+" ("+customerEmail+") completed registration."
				}
				log.info "Notification mail sent to Tradesman"
				return true;
			} catch(Exception e){
				log.info "Error while sending notification email to tradesman : " + e.message;
				e.printStackTrace();
				return false;
			}
		}
	}
	
	/**
	 * Send account creation confirmation mail to corresponding userType
	 * @param userType - TRADESMAN or CUSTOMER
	 * @param name - Tradesman contact name or Customer name
	 * @param emailId - Tradesman company email or custoomer email
	 * @param companyName - Tradesman Company Email
	 * @return boolean
	 */
	def sendConfirmationMail(String userType, String name, String emailId, String companyName){
		log.info "sendConfirmationMail()"
		try{
			if(userType == "TRADESMAN"){
				log.info "userType : TRADESMAN"
				mailService.sendMail{
					to emailId
					subject companyName+", thank you for Registering on TradeRefer"
					body(view: 'tradesmanConfirmationMail', model:[name:name, emailId:emailId, companyName:companyName])
				}
			} else if(userType == "CUSTOMER"){
				log.info "userType : CUSTOMER"
				mailService.sendMail{
					to emailId
					subject name+", thank you for Registering on TradeRefer"
					body(view: 'customerConfirmationMail', model:[name:name, emailId:emailId])
				}
			}
			log.info "Confirmation mail sent successfully to $userType";
			return true;
		} catch(Exception e){
			log.info "Error while sending confirmation email to tradesman : " + e.message;
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @param username - emailId entered as username to create new customer / tradesman account
	 * @return String - "Exists" - if username already exists / "Valid" - if username is valid
	 */
	def checkExistingUser(String username){
		log.info "checkExistingUser()"
		def user = User.findByUsername(username);
		log.info "User exists : " + user;
		if(user){
			render "Exists"
		} else{
			render "Valid"
		}
	}
	
	/**
	 * Renders bytes for logo image to email opened.
	 * WHich also determines that customer has read the email or not
	 * @param id - InvoiceNumber
	 * @return TradeRefer Logo Image
	 */
	def logo(String id){
		log.info "logo()"
		Emails email = Emails.findByInvoiceNumber(id);
		if(email){
			if(email?.emailOneSent){
				if(email?.emailTwoSent){
					if(!email?.emailTwoOpen){
						email.setEmailTwoOpen(true);
						email.save(flush:true);
					}
				} else{
					if(!email?.emailOneOpen){
						email.setEmailOneOpen(true);
						email.save(flush:true);
					}
				}
			}
		}
		byte[] image= assetResourceLocator?.findAssetForURI('TradeReferLogo.jpg')?.getInputStream()?.bytes
		if(image){
			response.setHeader('Content-length', "${image.length}")
			response.contentType = 'image/png' // or the appropriate image content type
			response.outputStream << image
			response.outputStream.flush()
		}
	}
	
	/**
	 * Renders bytes for logo image to email opened.
	 * WHich also determines that tradesman has read the email or not
	 * @param id - InvoiceNumber
	 * @return TradeRefer Logo Image
	 */
	def tradesman_logo(Long id){
		log.info "tradesman_logo()"
		Jobs job = Jobs.get(id);
		if(job){
			if(job?.jobCreationMailSent_one){
				if(job?.jobCreationMailSent_two){
					if(job?.jobCreationMailSent_three){
						job.setJobCreationMail_three_read(true);
						job.save(flush:true);
					} else{
						job.setJobCreationMail_two_read(true);
						job.save(flush:true);
					}
				} else{
					job?.setJobCreationMail_one_read(true);
					job.save(flush:true);
				}
			}
		}
		byte[] image= assetResourceLocator?.findAssetForURI('TradeReferLogo.jpg')?.getInputStream()?.bytes
		if(image){
			response.setHeader('Content-length', "${image.length}")
			response.contentType = 'image/png' // or the appropriate image content type
			response.outputStream << image
			response.outputStream.flush()
		}
	}
	
	/**
	 * When User logs in using Facebook, it creates or updates facebook user
	 */
	/**
	 * @return
	 */
	def createFbUser(){
		log.info "createFbUser() | params : $params";
		if(params.email && params.name && params.userId && params.accessToken){
			
			def baseUrl = "https://graph.facebook.com/me?access_token="+params.accessToken;
			
			def httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(baseUrl);
			def response = httpClient.execute(httpGet)
			
			def verificationResp = EntityUtils.toString(response.getEntity())
			verificationResp = new JsonSlurper().parseText(verificationResp);
			
			log.info "fb access token verification response : "+verificationResp;
			if( verificationResp.keySet().contains('error') ){
				log.info "access token is wrong";
				redirect controller:"home", action:"index";
			} else{
				log.info "access token is verified";
				User user = User.findByUsername(params.email);
				if(user){
					FacebookUser fbUser = FacebookUser.findByUser(user);
					Customer customer = Customer.findByUser(user);
					LinkedInUser liUser = LinkedInUser.findByUser(user);
					if(fbUser){
						fbUser.setAccessToken(params.accessToken);
						fbUser.setUID(params.userId);
						fbUser.save(flush:true);
						UserRole userRole = UserRole.findByUser(user);
						if(userRole){
							userRole.setRole(Role.findByAuthority("ROLE_FACEBOOK_CUSTOMER"));
							userRole.save(flush:true);
						} else{
							userRole = new UserRole(user, Role.findByAuthority("ROLE_FACEBOOK_CUSTOMER")).save(flush:true);
						}
						springSecurityService.reauthenticate user.username;
						redirect controller:"home", action:"index";
					} else if(customer || liUser){
						springSecurityService.reauthenticate user.username;
						redirect controller:"home", action:"index";
					} else{
						fbUser = new FacebookUser();
						fbUser.setName(params.name);
						fbUser.setEmail(params.email);
						fbUser.setAccessToken(params.accessToken);
						fbUser.setUser(user);
						fbUser.setUID(params.userId)
						fbUser.save(flush:true);
						UserRole userRole = UserRole.findByUser(user);
						if(userRole){
							userRole.setRole(Role.findByAuthority("ROLE_FACEBOOK_CUSTOMER"));
							userRole.save(flush:true);
						} else{
							userRole = new UserRole(user, Role.findByAuthority("ROLE_FACEBOOK_CUSTOMER")).save(flush:true);
						}
						springSecurityService.reauthenticate user.username;
						redirect controller:"home", action:"index";
					}
				} else{
					user = new User();
					user.setUsername(params.email);
					user.setPassword(params.accessToken);
					user.save(flush:true);
					
					UserRole userRole = new UserRole();
					userRole.setRole(Role.findByAuthority("ROLE_FACEBOOK_CUSTOMER"));
					userRole.setUser(user);
					userRole.save(flush:true);
					FacebookUser fbUser = new FacebookUser();
					fbUser.setName(params.name);
					fbUser.setEmail(params.email);
					fbUser.setAccessToken(params.accessToken);
					fbUser.setUID(params.userId);
					fbUser.setUser(user);
					fbUser.save(flush:true);
					def emailSent = sendConfirmationMail("CUSTOMER", params.name, params.email, null);
					if(emailSent == true){
						log.info "Confirmation Email sent successfully"
						Emails email = Emails.findByCustomerEmailAndInvoiceNumber(params.email, params.invoiceID);
						if(email){
							email.setCustomerRegistered(true);
							email.save(flush:true);
							def notifyTradesman = sendNotificationEmailToTradesman(fbUser?.name, fbUser?.email, email?.tradesmanEmail, "CustomerRegistration");
							if(notifyTradesman == true){
								springSecurityService.reauthenticate user.username
								redirect controller:"home", action:"index"
							} else{
								flash.message = "Error while sending notification email to Tradesman !!"
								springSecurityService.reauthenticate user.username
								redirect controller:"home", action:"index"
							}
						} else{
							springSecurityService.reauthenticate user.username
							redirect controller:"home", action:"index"
						}
					} else{
						log.info "Error while sending confirmation email"
						springSecurityService.reauthenticate user.username;
						redirect controller:"home", action:"index";
					}
				}
			}
		}
	}
	
	/**
	 * When User logs in using LinkedIn, it creates or updates linkedIn user
	 */
	def createLinkedInUser(){
		log.info "createLinkedInUser() | params : $params";
		if(params.email && params.name && params.userId){
			User user = User.findByUsername(params.email);
			if(user){
				FacebookUser fbUser = FacebookUser.findByUser(user);
				Customer customer = Customer.findByUser(user);
				LinkedInUser liUser = LinkedInUser.findByUser(user);
				if(liUser){
					liUser.setOriginalProPicURL(params.proPic);
					liUser.setThumbnailProPicURL(params.thumb);
					liUser.save(flush:true);
					UserRole userRole = UserRole.findByUser(user);
					if(userRole){
						userRole.setRole(Role.findByAuthority("ROLE_LINKEDIN_CUSTOMER"));
						userRole.save(flush:true);
					} else{
						userRole = new UserRole(user, Role.findByAuthority("ROLE_LINKEDIN_CUSTOMER")).save(flush:true);
					}
					springSecurityService.reauthenticate user.username;
					redirect controller:"home", action:"index";
				} else if(fbUser || customer){
					springSecurityService.reauthenticate user.username;
					redirect controller:"home", action:"index";
				} else{
					liUser = new LinkedInUser();
					liUser.setName(params.name);
					liUser.setEmail(params.email);
					liUser.setUID(params.userId);
					liUser.setUser(user);
					liUser.setOriginalProPicURL(params.proPic);
					liUser.setThumbnailProPicURL(params.thumb);
					liUser.save(flush:true);
					UserRole userRole = UserRole.findByUser(user);
					if(userRole){
						userRole.setRole(Role.findByAuthority("ROLE_LINKEDIN_CUSTOMER"));
						userRole.save(flush:true);
					} else{
						userRole = new UserRole(user, Role.findByAuthority("ROLE_LINKEDIN_CUSTOMER")).save(flush:true);
					}
					springSecurityService.reauthenticate user.username;
					redirect controller:"home", action:"index";
				}
			} else{
				user = new User();
				user.setUsername(params.email);
				user.setPassword(params.userId);
				user.save(flush:true);
				
				UserRole userRole = new UserRole();
				userRole.setRole(Role.findByAuthority("ROLE_LINKEDIN_CUSTOMER"));
				userRole.setUser(user);
				userRole.save(flush:true);
				LinkedInUser liUser = new LinkedInUser();
				liUser.setName(params.name);
				liUser.setEmail(params.email);
				liUser.setUID(params.userId);
				liUser.setUser(user);
				liUser.setOriginalProPicURL(params.proPic);
				liUser.setThumbnailProPicURL(params.thumb);
				liUser.save(flush:true);
				def emailSent = sendConfirmationMail("CUSTOMER", params.name, params.email, null);
				if(emailSent == true){
					log.info "Confirmation Email sent successfully"
					Emails email = Emails.findByCustomerEmailAndInvoiceNumber(params.email, params.invoiceID);
					if(email){
						email.setCustomerRegistered(true);
						email.save(flush:true);
						def notifyTradesman = sendNotificationEmailToTradesman(liUser?.name, liUser?.email, email?.tradesmanEmail, "CustomerRegistration");
						if(notifyTradesman == true){
							springSecurityService.reauthenticate user.username
							redirect controller:"home", action:"index"
						} else{
							flash.message = "Error while sending notification email to Tradesman !!"
							springSecurityService.reauthenticate user.username
							redirect controller:"home", action:"index"
						}
					} else{
						springSecurityService.reauthenticate user.username
						redirect controller:"home", action:"index"
					}
				} else{
					log.info "Error while sending confirmation email"
					springSecurityService.reauthenticate user.username;
					redirect controller:"home", action:"index";
				}
			}
		}
	}
	
	def loginWithLinkedIn(){
		
	}
	
	def getAccessToken(String code){
		if(code){
			//HttpResponse response = HttpRequest.post
			def httpClient = HttpClients.createDefault();
			HttpPost httpPost =  new HttpPost("https://www.linkedin.com/oauth/v2/accessToken");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
			urlParameters.add(new BasicNameValuePair("code", code));
			urlParameters.add(new BasicNameValuePair("redirect_uri", "https://traderefer.co.uk/newRegistrations/loginWithLinkedIn"));
			urlParameters.add(new BasicNameValuePair("client_id", "816bcn2rk1xhxf"));
			urlParameters.add(new BasicNameValuePair("client_secret", "w6bONB7rrJaVqpo2"));
			
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
			def response = httpClient.execute(httpPost);
			
			println "response from linkedin oauth : " + response.getStatusLine().getStatusCode();
			if(response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() =="200"){
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				println "response from linkedin oauth : " + result?.toString();
				def slurper = new JsonSlurper();
				def json_result = slurper.parseText(result?.toString());
				String accessToken = json_result?.access_token;
				println "access token : " + accessToken;
				if(accessToken){
					HttpGet httpGet = new HttpGet("https://api.linkedin.com/v1/people/~:(id,num-connections,picture-url,first_name,last_name,email_address,picture_urls::(original))?format=json");
					httpGet.setHeader("Connection", "Keep-Alive");
					httpGet.setHeader("Authorization", "Bearer "+accessToken);
					def profile_response = httpClient.execute(httpGet);
					if(profile_response.getStatusLine().getStatusCode() == "200" || profile_response.getStatusLine().getStatusCode() == 200){
						BufferedReader profile_reader = new BufferedReader(new InputStreamReader(profile_response.getEntity().getContent()));
						StringBuffer profile_buffer = new StringBuffer();
						String profile_line = "";
						while ((profile_line = profile_reader.readLine()) != null) {
							profile_buffer.append(profile_line);
						}
						println "response from linkedin oauth : " + profile_buffer?.toString();
						def json_profile = slurper.parseText(profile_buffer?.toString());
						String name = json_profile?.firstName + " " + json_profile?.lastName;
						String email = json_profile?.emailAddress;
						String userId = json_profile?.id;
						String proPic = "";
						String thumb = "";
						if(json_profile?.pictureUrls?._total > 0){
							thumb = json_profile?.pictureUrl;
							proPic = json_profile?.pictureUrls?.values?.get(0);
						}
						println "thumb : " + thumb;
						println "propic : " + proPic;
						
						redirect action:'createLinkedInUser', params:[name:name, email:email, userId:userId, proPic:proPic, thumb:thumb]
					}
				}
			}			
		}
	}
	
	def forgotPassword(){
		
	}
	
	def sendPasswordRecovery(){
		log.info("Params to reset password : " + params);
		if(params.email){
			User user = User.findByUsername(params.email);
			if(user){
				Customer customer = Customer.findByUser(user);
				Tradesman tradesman = Tradesman.findByUser(user);
				if(tradesman || customer){
					def registrationCode = new RegistrationCode(username: user.username);
					registrationCode.save(flush: true);
					
					String url = generateLink('resetPassword', [t: registrationCode.token]);
					try{
						def conf = SpringSecurityUtils.securityConfig;
						mailService.sendMail {
							to user.username
							subject conf.ui.forgotPassword.emailSubject
							body(view: '/layouts/forgotpasswordTemplate', model:[user: user,url:url])
						}
						flash.message = "Success"
						chain action:"forgotPassword", params:[contactEmail:params.email]
					} catch(Exception e){
						e.printStackTrace()
						flash.message = "Error occurred while sending reset password mail to $user.username"
						chain action:"forgotPassword", params:[contactEmail:params.email]
					}
				} else{
					flash.message = "Can not find Tradesman or Customer with given Email Address"
					chain action:"forgotPassword", params:[contactEmail:params.email]
				}
			} else{
				flash.message = "Can not find user with given Email Address"
				chain action:"forgotPassword", params:[contactEmail:params.email]
			}
			
		} else{
			flash.message = "Please Enter Valid Email Address"
			chain action:"forgotPassword", params:[contactEmail:params.email]
		}
	}
	
	protected String generateLink(String action, linkParams) {
		createLink(base: "$request.scheme://$request.serverName:$request.serverPort$request.contextPath",
		controller: 'newRegistrations', action: action,
		params: linkParams)
	}
	
	def resetPassword = { ResetPasswordCommand command ->
		String token = params.t
		def registrationCode = token ? RegistrationCode.findByToken(token) : null
		if (!registrationCode) {
			flash.error = message(code: 'spring.security.ui.resetPassword.badCode')
			redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
			return
		}
		
		if (!request.post) {
			return [token: token, command: new ResetPasswordCommand()]
		}
		
		command.username = registrationCode.username
		command.validate();
		
		if (command.hasErrors()) {
			return [token: token, command: command]
		}
		String salt = saltSource instanceof NullSaltSource ? null : registrationCode.username;
		String password=command.password.toString();
		RegistrationCode.withTransaction { status ->
			def user = User.findByUsername(registrationCode.username)
			user.password = springSecurityUiService.encodePassword(password,salt)
			user.save()
			registrationCode.delete()
		}
		
		springSecurityService.reauthenticate registrationCode.username
		
		flash.message = message(code: 'spring.security.ui.resetPassword.success')
		
		def conf = SpringSecurityUtils.securityConfig
		String postResetUrl = conf.ui.register.postResetUrl ?: conf.successHandler.defaultTargetUrl
		redirect uri: postResetUrl
	}
	
	static final password2Validator = { value, command ->
		if (command.password != command.password2) {
			return 'command.password2.error.mismatch'
		}
	}
}


class ResetPasswordCommand {
	String username
	String password
	String password2

	static constraints = {
		username nullable: false
		password blank: false, nullable: false
		password2 validator: NewRegistrationsController.password2Validator

	}
}