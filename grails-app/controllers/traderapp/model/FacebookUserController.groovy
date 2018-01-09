package traderapp.model

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.util.Environment
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

import org.springframework.beans.factory.annotation.Value

import traderapp.user.User

/**
 * All operations performed by Facebook User
 */
@Transactional(readOnly = true)
@Slf4j
class FacebookUserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def springSecurityService
	
	@Value('${amazonPay.redirectUrl}')
	String amazonPay_redirectUrl;
	
	
    /**
     * Redirects to profile page of facebook user
     */
    def index() {
        log.info "index()";
		User user = springSecurityService.currentUser;
		log.info "user : " +user;
		if(user){
			FacebookUser loggedInCustomer = FacebookUser.findByUser(user);
			log.info "logged in facebook user : " + loggedInCustomer;
			[fbUser:loggedInCustomer]
		}
    }

    def show(FacebookUser facebookUser) {
        respond facebookUser
    }

    def create() {
        respond new FacebookUser(params)
    }

    @Transactional
    def save(FacebookUser facebookUser) {
        if (facebookUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (facebookUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond facebookUser.errors, view:'create'
            return
        }

        facebookUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'facebookUser.label', default: 'FacebookUser'), facebookUser.id])
                redirect facebookUser
            }
            '*' { respond facebookUser, [status: CREATED] }
        }
    }

    def edit(FacebookUser facebookUser) {
        respond facebookUser
    }

    @Transactional
    def update(FacebookUser facebookUser) {
        if (facebookUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (facebookUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond facebookUser.errors, view:'edit'
            return
        }

        facebookUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'facebookUser.label', default: 'FacebookUser'), facebookUser.id])
                redirect facebookUser
            }
            '*'{ respond facebookUser, [status: OK] }
        }
    }

    @Transactional
    def delete(FacebookUser facebookUser) {

        if (facebookUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        facebookUser.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'facebookUser.label', default: 'FacebookUser'), facebookUser.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'facebookUser.label', default: 'FacebookUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	/**
	 * Redirects to list of jobs created by / for loggedin facebook user
	 */
	def showJobs(Integer max){
		log.info "showJobs()"
		params.max = Math.min(max ?: 10, 100);
		User user = springSecurityService.currentUser;
		FacebookUser loggedInCustomer = FacebookUser.findByUser(user);
		def jobs_count = Jobs.findAllByCustomerEmail(loggedInCustomer?.email)?.size();
		def jobs;
		if(!params.offset && params.sort && params.order){
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max, sort:params.sort, order:params.order]);
		} else if(!params.offset && !params.sort && !params.order){
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max]);
		} else{
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max, sort:params.sort, order:params.order, offset:params.offset]);
		}
		
		[jobs:jobs, fbUser:loggedInCustomer, jobsCount:jobs_count]
	}
	
	/**
	 * Renders profile picture of loggedin facebook user
	 */
	def viewProfilePic(){
		log.info "viewProfilePic()";
		def user = springSecurityService.currentUser;
		FacebookUser fbCustomer = FacebookUser.findByUser(user);
		byte[] image= fbCustomer.profilePic;
		if(image){
			response.setHeader('Content-length', "${image.length}")
			response.contentType = 'image/png' // or the appropriate image content type
			response.outputStream << image
			response.outputStream.flush()
		}
	}
	
	/**
	 * Updates FacebookUser instance with new values
	 * @param id - FacebookUser Instance
	 */
	def updateProfile(Long id){
		FacebookUser fbUser = FacebookUser.get(id);
		User user = fbUser.user;
		log.info "updateProfile() | Params for update profile : " + params;
		try{
			log.info "Updating FBCustomer Profile..";
			fbUser.setName(params.name);
			fbUser.setAddressLine1(params.addressLine1);
			fbUser.setAddressLine2(params.addressLine2);
			fbUser.setAddressLine3(params.addressLine3);
			fbUser.setPostcode(params.postcode);
			fbUser.setPhone(params.phone);
			//fbUser.setPaymentMethod(params.paymentMethod);
			fbUser.save(flush:true);
			
			fbUser.paymentMethod = null;
			
			if(params.paymentMethod_paypal == "on"){
				log.info ("Payment method : PayPal");
				fbUser.addToPaymentMethod("PayPal");
			}
			if(params.paymentMethod_amazonpay == "on"){
				log.info ("Payment method : AmazonPay");
				fbUser.addToPaymentMethod("AmazonPay");
			}
			
			fbUser.save(flush:true);
		} catch(Exception e){
			e.printStackTrace();
		}
		redirect action:'index';
	}
	
	/**
	 * Upload profile picture for FacebookUser instance
	 */
	def uploadProfilePicture(){
		log.info "uploadProfilePicture()"
		User user = springSecurityService.currentUser;
		FacebookUser loggedInCustomer = FacebookUser.findByUser(user);
		def file = request.getFile('featuredImageFile');
		loggedInCustomer.profilePic = file.bytes;
		loggedInCustomer.save(flush:true);
		redirect action:'index';
	}
	
	/**
	 * @param id - Job Instance id
	 * Renders template with values to show Invoice in modal
	 */
	def viewInvoiceById(Long id){
		log.info "viewInvoiceById()"
		User user = springSecurityService.currentUser;
		FacebookUser fbUser = FacebookUser.findByUser(user);
		Jobs job = Jobs.get(id);
		String job_date = "";
		if(job?.dateOfJob != null && job?.dateOfJob != ""){
			Date date = job.dateOfJob;
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			job_date = sdf.format(date);
		}
		Invoice invoice = job?.invoice;
		String redirectUrl = "";
		redirectUrl = amazonPay_redirectUrl;
		AdminSettings setting = AdminSettings.get(1);
		render template: 'viewInvoice', model:[setting:setting, job:job, fbUser:fbUser, job_date:job_date, invoiceInstance:invoice, tradesmanInstance:job?.tradesman, redirectUrl:redirectUrl]
	}
	
	/**
	 * Redirects to Success page after successful transaction by 
	 * AmazonPay or PayPal
	 */
	def paymentSuccess(){
		User user = springSecurityService.currentUser;
		log.info "paymentSuccess() | user : " +user;
		if(user){
			FacebookUser loggedInCustomer = FacebookUser.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			[fbUser:loggedInCustomer, invoice:invoice]
		}
	}
	
	/**
	 * Redirects to Cancel page after cancelled transaction by 
	 * AmazonPay or PayPal
	 */
	def paymentCancelled(){
		User user = springSecurityService.currentUser;
		log.info "paymentCancelled() | user : " +user;
		if(user){
			FacebookUser loggedInCustomer = FacebookUser.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			[fbUser:loggedInCustomer, invoice:invoice]
		}
	}
	
	/**
	 * Redirects to Error page after failed transaction by 
	 * AmazonPay or PayPal
	 */
	def paymentError(){
		User user = springSecurityService.currentUser;
		log.info "paymentError() | user : " +user;
		if(user){
			FacebookUser loggedInCustomer = FacebookUser.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			if(params.invoiceId){
				Invoice invoice = Invoice.get(params.invoiceId?.toLong());
				[fbUser:loggedInCustomer, invoice:invoice]
			} else{
				[fbUser:loggedInCustomer, invoice:null]
			}
		}
	}
}
