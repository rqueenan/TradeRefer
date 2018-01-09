package traderapp.model

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.util.Environment
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

import org.springframework.beans.factory.annotation.Value

import traderapp.user.User

/**
 * All operations performed by customer
 */
@Transactional(readOnly = true)
@Slf4j
class CustomerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def springSecurityService;
	
	@Value('${amazonPay.redirectUrl}')
	String amazonPay_redirectUrl;
	
    /**
     * Redirects to profile page of customer
     */
    def index() {
		log.info "index()"
        User user = springSecurityService.currentUser;
		log.info "user : " +user;
		if(user){
			Customer loggedInCustomer = Customer.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			[customerInstance:loggedInCustomer]
		}
    }
	
	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		respond Customer.list(params), model:[customerCount: Customer.count()]
	}

    def show(Customer customer) {
        respond customer
    }

    def create() {
        respond new Customer(params)
    }

    @Transactional
    def save(Customer customer) {
        if (customer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (customer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond customer.errors, view:'create'
            return
        }

        customer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'customer.label', default: 'Customer'), customer.id])
                redirect customer
            }
            '*' { respond customer, [status: CREATED] }
        }
    }

    def edit(Customer customer) {
        respond customer
    }

    @Transactional
    def update(Customer customer) {
        if (customer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (customer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond customer.errors, view:'edit'
            return
        }

        customer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'customer.label', default: 'Customer'), customer.id])
                redirect customer
            }
            '*'{ respond customer, [status: OK] }
        }
    }

    @Transactional
    def delete(Customer customer) {

        if (customer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        customer.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'customer.label', default: 'Customer'), customer.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	/**
	 * Updates customerInstance with new Values 
	 * @param id - CustomerInstance Id
	 * @return redirects back to profile page of customer
	 */
	def updateProfile(Long id){
		log.info "updateProfile()"
		Customer customer = Customer.get(id);
		User user = customer.user;
		log.info "Params for update profile : " + params;
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
	 * Redirects to list of jobs created by / for logged in customer
	 */
	def showJobs(Integer max){
		log.info "showJobs()"
		params.max = Math.min(max ?: 10, 100);
		User user = springSecurityService.currentUser;
		Customer loggedInCustomer = Customer.findByUser(user);
		def jobs_count = Jobs.findAllByCustomerEmail(loggedInCustomer?.email).size();
		def jobs;
		if(!params.offset && params.sort && params.order){
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max, sort:params.sort, order:params.order]);
		} else if(!params.offset && !params.sort && !params.order){
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max]);
		} else{
			jobs = Jobs.findAllByCustomerEmail(loggedInCustomer?.email, [max:params.max, sort:params.sort, order:params.order, offset:params.offset]);
		}
		[jobs:jobs, customerInstance:loggedInCustomer, jobsCount:jobs_count]
	}
	
	/**
	 * @param id - Job Instance id
	 * Renders template with values to show Invoice in modal
	 */
	def viewInvoiceById(Long id){
		log.info "viewInvoiceById()"
		User user = springSecurityService.currentUser;
		Customer customer = Customer.findByUser(user);
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
		render template: 'viewInvoice', model:[setting:setting, job:job, customerInstance:customer, job_date:job_date, invoiceInstance:invoice, tradesmanInstance:job?.tradesman, redirectUrl:redirectUrl]
	}
	
	/**
	 * Redirects to Success page after successful transaction by 
	 * AmazonPay or PayPal
	 */
	def paymentSuccess(){
		log.info "paymentSuccess()"
		User user = springSecurityService.currentUser;
		log.info "user : " +user;
		if(user){
			Customer loggedInCustomer = Customer.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			[customerInstance:loggedInCustomer, invoice:invoice]
		}
	}
	
	/**
	 * Redirects to Cancel page after cancelled transaction by 
	 * AmazonPay or PayPal
	 */
	def paymentCancelled(){
		log.info "paymentCancelled()"
		User user = springSecurityService.currentUser;
		log.info "user : " +user;
		if(user){
			Customer loggedInCustomer = Customer.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			Invoice invoice = Invoice.get(params.invoiceId?.toLong());
			[customerInstance:loggedInCustomer, invoice:invoice]
		}
	}
	
	/**
	 * Redirects to Error page after failed transaction by 
	 * AmazonPay or PayPal
	 */
	def paymentError(){
		log.info "paymentError()"
		User user = springSecurityService.currentUser;
		log.info "user : " +user;
		if(user){
			Customer loggedInCustomer = Customer.findByUser(user);
			log.info "logged in customer : " + loggedInCustomer;
			if(params.invoiceId){
				Invoice invoice = Invoice.get(params.invoiceId?.toLong());
				[customerInstance:loggedInCustomer, invoice:invoice]
			} else{
				[customerInstance:loggedInCustomer, invoice:null]
			}
		}
	}
}
