package traderapp.model

import groovy.util.logging.Slf4j
import traderapp.user.Role
import traderapp.user.User
import traderapp.user.UserRole

@Slf4j
class ProfileController {
	def springSecurityService;
	def invoiceEmailsService;
	
    def index() { 
		User user = springSecurityService.currentUser;
		UserRole user_role = UserRole.findByUser(user);
		Role role = user_role?.role;
		log.info "user : " +user;
		def customer;
		if(role?.authority == "ROLE_CUSTOMER"){
			if(user){
				customer = Customer.findByUser(user);
				log.info "logged in customer : " + customer;
			}
		} else if(role?.authority == "ROLE_FACEBOOK_CUSTOMER"){
			if(user){
				customer = FacebookUser.findByUser(user);
				log.info "logged in fb user : " + customer;
			}
		} else if(role?.authority == "ROLE_LINKEDIN_CUSTOMER"){
			if(user){
				customer = LinkedInUser.findByUser(user);
				log.info "logged in li user : " + customer;
			}
		}
		[role:role?.authority, customer:customer]
	}
	
	def jobs(Integer max){
		log.info "showJobs()"
		params.max = Math.min(max ?: 10, 100);
		User user = springSecurityService.currentUser;
		UserRole user_role = UserRole.findByUser(user);
		Role role = user_role?.role;
		log.info "user : " +user;
		def customer;
		if(role?.authority == "ROLE_CUSTOMER"){
			if(user){
				customer = Customer.findByUser(user);
				log.info "logged in customer : " + customer;
			}
		} else if(role?.authority == "ROLE_FACEBOOK_CUSTOMER"){
			if(user){
				customer = FacebookUser.findByUser(user);
				log.info "logged in fb user : " + customer;
			}
		} else if(role?.authority == "ROLE_LINKEDIN_CUSTOMER"){
			if(user){
				customer = LinkedInUser.findByUser(user);
				log.info "logged in li user : " + customer;
			}
		}
		
		def jobs_count = Jobs.findAllByCustomerEmail(customer?.email).size();
		def jobs = Jobs.createCriteria();
		def results;
		String order_str = "";
		String sort = "";
		if(!params.order && !params.sort){
			order_str = "desc";
			sort = "creationDate";
		} else{
			order_str = params.order;
			sort = params.sort;
		}
		if(params.offset){
			results = jobs.list(max: params.max, offset: params.offset){
				eq("customerEmail", customer?.email)
				if(sort && order_str){
					order(sort, order_str)
				}
			}
		} else{
			results = jobs.list(max: params.max){
				eq("customerEmail", customer?.email)
				if(sort && order_str){
					order(sort, order_str)
				}
			}
		}
		[role:role?.authority, jobs:results, customer:customer, jobsCount:jobs_count]
	}
	
	/**
	 * AmazonPay and PayPal Successful transactions are
	 * redirected to corresponding customer's success page
	 */
	def success(){
		def user = springSecurityService.currentUser
		log.info "showSuccess() | $user";
		if(user){
			log.info "user id : " + user?.id;
			UserRole loggedInUserRole = UserRole.findByUser(user);
			Role loggedInRole = loggedInUserRole?.role;
			log.info "Logged in user role is : " + loggedInRole.authority;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			Jobs job = Jobs.findByInvoice(invoice);
			log.info "Sending Successful Invoice Payment Notification to Tradesman"
			invoiceEmailsService.sendPaymentEmailToTradesman(job, invoice);
			def customer;
			if (loggedInRole.authority == 'ROLE_CUSTOMER') {
				customer = Customer.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_FACEBOOK_CUSTOMER'){
				customer = FacebookUser.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_LINKEDIN_CUSTOMER'){
				customer = LinkedInUser.findByUser(user);
			}
			[customer:customer, invoice:invoice]
		}
	}
	
	/**
	 * AmazonPay and PayPal Failed transactions are
	 * redirected to corresponding customer's error page
	 */
	def error(){
		def user = springSecurityService.currentUser
		log.info "showError() | $user";
		if(user){
			log.info "user id : " + user?.id;
			UserRole loggedInUserRole = UserRole.findByUser(user);
			Role loggedInRole = loggedInUserRole?.role;
			log.info "Logged in user role is : " + loggedInRole.authority;
			Invoice invoice;
			if(params.invoiceId){
				invoice = Invoice.get(params.invoiceId?.toLong());
			} else{
				invoice = null;	
			} 
			def customer;
			if (loggedInRole.authority == 'ROLE_CUSTOMER') {
				customer = Customer.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_FACEBOOK_CUSTOMER'){
				customer = FacebookUser.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_LINKEDIN_CUSTOMER'){
				customer = LinkedInUser.findByUser(user);
			}
			[customer:customer, invoice:invoice]
		}
	}
	
	
	/**
	 * AmazonPay and PayPal Cancelled transactions are
	 * redirected to corresponding customer's cancelled page
	 */
	def cancel(){
		def user = springSecurityService.currentUser
		if(user){
			log.info "user id : " + user?.id;
			UserRole loggedInUserRole = UserRole.findByUser(user);
			Role loggedInRole = loggedInUserRole?.role;
			log.info "Logged in user role is : " + loggedInRole.authority;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			def customer;
			if (loggedInRole.authority == 'ROLE_CUSTOMER') {
				customer = Customer.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_FACEBOOK_CUSTOMER'){
				customer = FacebookUser.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_LINKEDIN_CUSTOMER'){
				customer = LinkedInUser.findByUser(user);
			}
			[customer:customer, invoice:invoice]
		}
	}
	
	def paid(){
		def user = springSecurityService.currentUser
		if(user){
			log.info "user id : " + user?.id;
			UserRole loggedInUserRole = UserRole.findByUser(user);
			Role loggedInRole = loggedInUserRole?.role;
			log.info "Logged in user role is : " + loggedInRole.authority;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			def customer;
			if (loggedInRole.authority == 'ROLE_CUSTOMER') {
				customer = Customer.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_FACEBOOK_CUSTOMER'){
				customer = FacebookUser.findByUser(user);
			} else if(loggedInRole.authority == 'ROLE_LINKEDIN_CUSTOMER'){
				customer = LinkedInUser.findByUser(user);
			}
			[customer:customer, invoice:invoice]
		}
	}
	
	/**
	 * Renders profile picture of logged in customer
	 */
	def viewProfilePic(){
		log.info "viewProfilePic()"
		User user = springSecurityService.currentUser;
		UserRole user_role = UserRole.findByUser(user);
		Role role = user_role?.role;
		log.info "user : " +user;
		def customer;
		if (role.authority == 'ROLE_CUSTOMER') {
			customer = Customer.findByUser(user);
		} else if(role.authority == 'ROLE_FACEBOOK_CUSTOMER'){
			customer = FacebookUser.findByUser(user);
		} else if(role.authority == 'ROLE_LINKEDIN_CUSTOMER'){
			customer = LinkedInUser.findByUser(user);
		}
		
		byte[] image= customer.profilePic;
		if(image){
			response.setHeader('Content-length', "${image.length}")
			response.contentType = 'image/png' // or the appropriate image content type
			response.outputStream << image
			response.outputStream.flush()
		}
	}
	
	/**
	 * Updates customerInstance with new Values
	 * @param id - CustomerInstance Id
	 * @return redirects back to profile page of customer
	 */
	def updateProfile(Long id){
		User user = springSecurityService.currentUser;
		UserRole user_role = UserRole.findByUser(user);
		Role role = user_role?.role;
		log.info "user : " +user;
		def customer;
		if (role.authority == 'ROLE_CUSTOMER') {
			customer = Customer.get(id);
		} else if(role.authority == 'ROLE_FACEBOOK_CUSTOMER'){
			customer = FacebookUser.get(id);
		} else if(role.authority == 'ROLE_LINKEDIN_CUSTOMER'){
			customer = LinkedInUser.get(id);
		}
		try{
			log.info "Updating Customer Profile";
			customer.setName(params.name);
			customer.setAddressLine1(params.addressLine1);
			customer.setAddressLine2(params.addressLine2);
			customer.setAddressLine3(params.addressLine3);
			customer.setPostcode(params.postcode);
			customer.setPhone(params.phone);
			//customer.setPaymentMethod(params.paymentMethod);
			customer.save(flush:true);
			
			customer.paymentMethod = null;
			
			if(params.paymentMethod_paypal == "on"){
				log.info ("Payment method : PayPal");
				customer.addToPaymentMethod("PayPal").save(fluh:true);
			}
			if(params.paymentMethod_amazonpay == "on"){
				log.info ("Payment method : AmazonPay");
				customer.addToPaymentMethod("AmazonPay").save(fluh:true);
			}
			
			customer.save(flush:true);
			log.info "Done updating customer profile"
		} catch(Exception e){
			log.info "Error while updating cuustomer profile : " + e.message;
			e.printStackTrace();
		}
		redirect controller:'profile', action:'index';
	}
	
	
	/**
	 * Upload new profile picture for customer
	 */
	def uploadProfilePicture(){
		User user = springSecurityService.currentUser;
		UserRole user_role = UserRole.findByUser(user);
		Role role = user_role?.role;
		log.info "user : " +user;
		def customer;
		if (role.authority == 'ROLE_CUSTOMER') {
			customer = Customer.findByUser(user);
		} else if(role.authority == 'ROLE_FACEBOOK_CUSTOMER'){
			customer = FacebookUser.findByUser(user);
		} else if(role.authority == 'ROLE_LINKEDIN_CUSTOMER'){
			customer = LinkedInUser.findByUser(user);
		}
		def file = request.getFile('featuredImageFile');
		log.info "Original file name  : " + file.getOriginalFilename();
		String contentType = params.featuredImageFile.getContentType();
		log.info "File Content Type : " + contentType;
		if(contentType == 'image/jpeg' || contentType == 'image/jpg' || contentType == 'image/gif' || contentType == 'image/png' || contentType == 'image/bmp' || contentType?.contains("image")){
			customer.profilePic = file.bytes;
			customer.save(flush:true);
		}
		
		redirect controller:'profile', action:'index';
	}
}
