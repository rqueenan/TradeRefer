package traderapp.model

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.util.Environment
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

import org.springframework.beans.factory.annotation.Value

import traderapp.user.User

/**
 * All operations performed by LinkedIn User
 */
@Transactional(readOnly = true)
@Slf4j
class LinkedInUserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def springSecurityService
	
	@Value('${amazonPay.redirectUrl}')
	String amazonPay_redirectUrl;

    def index() {
        User user = springSecurityService.currentUser;
		println "user : " +user;
		if(user){
			LinkedInUser loggedInCustomer = LinkedInUser.findByUser(user);
			println "logged in customer : " + loggedInCustomer;
			[liUser:loggedInCustomer]
		}
    }

    def show(LinkedInUser linkedInUser) {
        respond linkedInUser
    }

    def create() {
        respond new LinkedInUser(params)
    }

    @Transactional
    def save(LinkedInUser linkedInUser) {
        if (linkedInUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (linkedInUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond linkedInUser.errors, view:'create'
            return
        }

        linkedInUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'linkedInUser.label', default: 'LinkedInUser'), linkedInUser.id])
                redirect linkedInUser
            }
            '*' { respond linkedInUser, [status: CREATED] }
        }
    }

    def edit(LinkedInUser linkedInUser) {
        respond linkedInUser
    }

    @Transactional
    def update(LinkedInUser linkedInUser) {
        if (linkedInUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (linkedInUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond linkedInUser.errors, view:'edit'
            return
        }

        linkedInUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'linkedInUser.label', default: 'LinkedInUser'), linkedInUser.id])
                redirect linkedInUser
            }
            '*'{ respond linkedInUser, [status: OK] }
        }
    }

    @Transactional
    def delete(LinkedInUser linkedInUser) {

        if (linkedInUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        linkedInUser.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'linkedInUser.label', default: 'LinkedInUser'), linkedInUser.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'linkedInUser.label', default: 'LinkedInUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	/**
	 * Redirects to list of jobs created by / for logged in LinkedIn User
	 */
	def showJobs(Integer max){
		log.info "showJobs()";
		params.max = Math.min(max ?: 10, 100);
		User user = springSecurityService.currentUser;
		LinkedInUser loggedInCustomer = LinkedInUser.findByUser(user);
		def jobs_count = Jobs.findAllByCustomerEmail(loggedInCustomer?.email).size();
		def jobs;
		if(!params.offset && params.sort && params.order){
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max, sort:params.sort, order:params.order]);
		} else if(!params.offset && !params.sort && !params.order){
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max]);
		} else{
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max, sort:params.sort, order:params.order, offset:params.offset]);
		}
		[jobs:jobs, liUser:loggedInCustomer, jobsCount:jobs_count]
	}
	
	/**
	 * Renders profile picture of logged in linkedIn user
	 */
	def viewProfilePic(){
		log.info "viewProfilePic()"
		def user = springSecurityService.currentUser;
		LinkedInUser liCustomer = LinkedInUser.findByUser(user);
		byte[] image= liCustomer.profilePic;
		if(image){
			response.setHeader('Content-length', "${image.length}")
			response.contentType = 'image/png' // or the appropriate image content type
			response.outputStream << image
			response.outputStream.flush()
		}
	}
	
	/**
	 * Updates LinkedIn User instance with new values
	 * @param id LinkedInUser Instance id
	 */
	def updateProfile(Long id){
		log.info "updateProfile()";
		LinkedInUser liUser = LinkedInUser.get(id);
		User user = liUser.user;
		log.info "Params for update profile : " + params;
		try{
			log.info "Updating LICustomer Profile..";
			liUser.setName(params.name);
			liUser.setAddressLine1(params.addressLine1);
			liUser.setAddressLine2(params.addressLine2);
			liUser.setAddressLine3(params.addressLine3);
			liUser.setPostcode(params.postcode);
			liUser.setPhone(params.phone);
			//liUser.setPaymentMethod(params.paymentMethod);
			liUser.save(flush:true);
			
			liUser.paymentMethod = null;
			
			if(params.paymentMethod_paypal == "on"){
				log.info ("Payment method : PayPal");
				liUser.addToPaymentMethod("PayPal");
			}
			if(params.paymentMethod_amazonpay == "on"){
				log.info ("Payment method : AmazonPay");
				liUser.addToPaymentMethod("AmazonPay");
			}
			
			liUser.save(flush:true);
		} catch(Exception e){
			e.printStackTrace();
		}
		redirect action:'index';
	}
	
	/**
	 * Upload profile picture for LinkedInUser instance
	 */
	def uploadProfilePicture(){
		log.info "uploadProfilePicture()"
		User user = springSecurityService.currentUser;
		LinkedInUser loggedInCustomer = LinkedInUser.findByUser(user);
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
		log.info "viewInvoiceById()";
		User user = springSecurityService.currentUser;
		LinkedInUser liUser = LinkedInUser.findByUser(user);
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
		render template: 'viewInvoice', model:[setting:setting, job:job, liUser:liUser, job_date:job_date, invoiceInstance:invoice, tradesmanInstance:job?.tradesman, redirectUrl:redirectUrl]
	}
	
	/**
	 * Redirects to Success page after successful transaction by
	 * AmazonPay or PayPal
	 */
	def paymentSuccess(){
		User user = springSecurityService.currentUser;
		log.info "paymentSuccess() | user : " +user;
		if(user){
			LinkedInUser loggedInCustomer = LinkedInUser.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			[liUser:loggedInCustomer, invoice:invoice]
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
			LinkedInUser loggedInCustomer = LinkedInUser.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			[liUser:loggedInCustomer, invoice:invoice]
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
			LinkedInUser loggedInCustomer = LinkedInUser.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			if(params.invoiceId){
				Invoice invoice = Invoice.get(params.invoiceId?.toLong());
				[liUser:loggedInCustomer, invoice:invoice]
			} else{
				[liUser:loggedInCustomer, invoice:null]
			}
		}
	}
}
