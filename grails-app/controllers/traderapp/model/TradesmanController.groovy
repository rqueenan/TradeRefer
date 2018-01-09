package traderapp.model

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import groovy.util.logging.Slf4j

import java.text.DecimalFormat
import java.text.SimpleDateFormat

import org.apache.commons.lang.StringUtils

import traderapp.user.User

/**
 * All operations performed by tradesman
 */
@Transactional(readOnly = true)
@Slf4j
class TradesmanController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def springSecurityService;
	static DecimalFormat twoDForm = new DecimalFormat("0.00");
    /**
     * Redirects to profile tab of tradesman
     */
    def index() {
        User user = springSecurityService.currentUser;
		log.info "index() | user : " +user;
		if(user){
			Tradesman loggedInTradesman = Tradesman.findByUser(user);
			log.info "logged in tradesman : " + loggedInTradesman;
			log.info "work types in logged in tradesman : " + loggedInTradesman.workType;
			[tradesmanInstance:loggedInTradesman]
		}
    }
	
	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		respond Tradesman.list(params), model:[tradesmanCount: Tradesman.count()]
	}

    def show(Tradesman tradesman) {
        respond tradesman
    }

    def create() {
        respond new Tradesman(params)
    }

    @Transactional
    def save(Tradesman tradesman) {
        if (tradesman == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (tradesman.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond tradesman.errors, view:'create'
            return
        }

        tradesman.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tradesman.label', default: 'Tradesman'), tradesman.id])
                redirect tradesman
            }
            '*' { respond tradesman, [status: CREATED] }
        }
    }

    def edit(Tradesman tradesman) {
        respond tradesman
    }

    @Transactional
    def update(Tradesman tradesman) {
        if (tradesman == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (tradesman.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond tradesman.errors, view:'edit'
            return
        }

        tradesman.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'tradesman.label', default: 'Tradesman'), tradesman.id])
                redirect tradesman
            }
            '*'{ respond tradesman, [status: OK] }
        }
    }

    @Transactional
    def delete(Tradesman tradesman) {

        if (tradesman == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        tradesman.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'tradesman.label', default: 'Tradesman'), tradesman.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tradesman.label', default: 'Tradesman'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	/**
	 * Edit and update profile for tradesman
	 * @param id - Tradesman Instance id
	 */
	def updateProfile(Long id){
		Tradesman tradesman = Tradesman.get(id);
		User user = tradesman.user;
		log.info "updateProfile() | Params for update profile : " + params;
		try{
			log.info "Updating Tradesman profile..";
			tradesman.setName(params.name);
			tradesman.setCompanyName(params.companyName);
			tradesman.setAddressLine1(params.addressLine1);
			tradesman.setAddressLine2(params.addressLine2);
			tradesman.setAddressLine3(params.addressLine3);
			tradesman.setPostcode(params.postcode);
			tradesman.setPhone(params.phone);
			tradesman.setCompanyNumber(params.companyNumber);
			tradesman.setCertificaionNumber(params.certificaionNumber);
			if(!StringUtils.isBlank(params.hourlyRate)){
				tradesman.setHourlyRate(params.hourlyRate?.toDouble());
			}
			
			tradesman.setCollection_sortCode(params.collection_sortCode);
			tradesman.setCollection_accountName(params.collection_accountName);
			tradesman.setCollection_accountNo(params.collection_accountNo);
			tradesman.setPaypal_clientId(params.paypal_clientId);
			tradesman.setPaypal_clientSecret(params.paypal_clientSecret);
			tradesman.setAmazon_merchantId(params.amazon_merchantId);
			tradesman.setAmazon_accessKey(params.amazon_accessKey);
			tradesman.setAmazon_secretKey(params.amazon_secretKey);
			tradesman.setAmazon_clientId(params.amazon_clientId);
			
			if(params.paypalEnabled == "on"){
				tradesman.setPaypalEnabled(true);
			} else{
				tradesman.setPaypalEnabled(false);
			}
			if(params.amazonPayEnabled == "on"){
				tradesman.setAmazonPayEnabled(true);
			} else{
				tradesman.setAmazonPayEnabled(false);
			}
			tradesman.save(flush:true);
			
			tradesman.workType = null;
			
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
		} catch(Exception e){
			log.info "Error while updating profile for tradesman : " + e.message;
			e.printStackTrace();
		}
		
		redirect action:'index'
	}
	
	/**
	 * Redirects to the list of jobs created by / for tradesman
	 */
	def showJobs(Integer max){
		log.info "showJobs()";
		params.max = Math.min(max ?: 10, 100);
		User user = springSecurityService.currentUser;
		Tradesman loggedInTradesman = Tradesman.findByUser(user);
		def jobs_count = Jobs.findAllByTradesman(loggedInTradesman).size();
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
				eq("tradesman", loggedInTradesman)
				if(sort && order_str){
					order(sort, order_str)
				}
			}
		} else{
			log.info "order : " + order_str + " sort : " + sort;
			
			results = jobs.list(max: params.max){
				eq("tradesman", loggedInTradesman)
				if(sort && order_str){
					order(sort, order_str)
				}
			}
			
			log.info "results : $results";
		}
		
		def hourlRate2D = "";
		if(loggedInTradesman?.hourlyRate){
			hourlRate2D = twoDForm.format(loggedInTradesman?.hourlyRate);
		} 
		[jobs:results, tradesmanInstance:loggedInTradesman, hourlRate2D:hourlRate2D, jobsCount:jobs_count, max:params.max, sort:params.sort, order:params.order, offset:params.offset]
	}
	
	/**
	 * Render profile picture saved for tradesman
	 */
	def viewProfilePic(){
		log.info "viewProfilePic()"
		def user = springSecurityService.currentUser;
		Tradesman tradesman = Tradesman.findByUser(user);
		byte[] image= tradesman.profilePic;
		if(image){
			response.setHeader('Content-length', "${image.length}")
			response.contentType = 'image/png' // or the appropriate image content type
			response.outputStream << image
			response.outputStream.flush()
		}
	}
	
	/**
	 * Upload profile picture for tradesman
	 */
	def uploadProfilePicture(){
		log.info "uploadProfilePicture()"
		User user = springSecurityService.currentUser;
		Tradesman loggedInTradesman = Tradesman.findByUser(user);		
		def file = request.getFile('featuredImageFile');
		log.info "Original file name  : " + file.getOriginalFilename();
		String contentType = params.featuredImageFile.getContentType();
		log.info "File Content Type : " + contentType;
		if(contentType == 'image/jpeg' || contentType == 'image/jpg' || contentType == 'image/gif' || contentType == 'image/png' || contentType == 'image/bmp' || contentType?.contains("image")){
			loggedInTradesman.profilePic = file.bytes;
			loggedInTradesman.save(flush:true);			
		}
		redirect action:'index';
	}
	
	/**
	 * @param id - Jobs Instance id
	 * @return template for edit job with pre populated values for job and tradesman instance
	 */
	def getJobById(Long id){
		log.info "getJobById()"
		User user = springSecurityService.currentUser;
		Tradesman tradesmanInstance = Tradesman.findByUser(user);
		Jobs job = Jobs.get(id);
		String job_date = "";
		if(job?.dateOfJob != null && job?.dateOfJob != ""){
			Date date = job.dateOfJob;
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			job_date = sdf.format(date);
		}
		def hourlRate2D = "";
		if(tradesmanInstance?.hourlyRate){
			hourlRate2D = twoDForm.format(tradesmanInstance?.hourlyRate);
		} 
		render template: 'editJob', model:[hourlRate2D:hourlRate2D, job:job, tradesmanInstance:tradesmanInstance, job_date:job_date, max:params.max, sort:params.sort, order:params.order, offset:params.offset]
	}
	
	/**
	 * @param id - Jobs Instance id
	 * @return template for create invoice for selected job instance
	 */
	def getJobByIDForInvoice(Long id){
		log.info "getJobByIDForInvoice()"
		User user = springSecurityService.currentUser;
		Tradesman tradesmanInstance = Tradesman.findByUser(user);
		Jobs job = Jobs.get(id);
		String job_date = "";
		if(job?.dateOfJob != null && job?.dateOfJob != ""){
			Date date = job.dateOfJob;
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			job_date = sdf.format(date);
		}
		AdminSettings setting = AdminSettings.get(1);
		def hourlRate2D = "";
		if(tradesmanInstance?.hourlyRate){
			hourlRate2D = twoDForm.format(tradesmanInstance?.hourlyRate);
		}
		render template: 'invoice', model:[hourlRate2D:hourlRate2D, setting:setting, job:job, tradesmanInstance:tradesmanInstance, job_date:job_date, max:params.max, sort:params.sort, order:params.order, offset:params.offset]
	}
	
	/**
	 * @param id - Jobs Instance id
	 * @return template to view invoice created for selected job
	 */
	def viewInvoiceById(Long id){
		log.info "viewInvoiceById()"
		User user = springSecurityService.currentUser;
		Tradesman tradesmanInstance = Tradesman.findByUser(user);
		Jobs job = Jobs.get(id);
		String job_date = "";
		if(job?.dateOfJob != null && job?.dateOfJob != ""){
			Date date = job.dateOfJob;
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			job_date = sdf.format(date);
		}
		Invoice invoice = job?.invoice;
		AdminSettings setting = AdminSettings.get(1);
		render template: 'viewInvoice', model:[setting:setting, job:job, tradesmanInstance:tradesmanInstance, job_date:job_date, invoiceInstance:invoice]
	}
	
	/**
	 * @param invoiceId - Invoice instance id
	 * @param jobId - jobs instance id
	 * @return template to edit selected invoice
	 */
	def getInvoiceInstanceForEdit(Long invoiceId, Long jobId){
		log.info "getInvoiceInstanceForEdit()"
		User user = springSecurityService.currentUser;
		Tradesman tradesmanInstance = Tradesman.findByUser(user);
		Jobs job = Jobs.get(jobId);
		Invoice invoice = Invoice.get(invoiceId);
		AdminSettings setting = AdminSettings.get(1);
		def hourlRate2D = "";
		if(tradesmanInstance?.hourlyRate){
			hourlRate2D = twoDForm.format(tradesmanInstance?.hourlyRate);
		}
		render template: 'editInvoice', model:[hourlRate2D:hourlRate2D, setting:setting, job:job, tradesmanInstance:tradesmanInstance, invoiceInstance:invoice, max:params.max, sort:params.sort, order:params.order, offset:params.offset]
	}
	
	def viewInvoice(Long id){
		if(id){
			Invoice invoice =  Invoice.get(id);
			if(invoice){
				Jobs job = Jobs.findByInvoice(invoice);
				if(job){
					Tradesman tradesman = job?.tradesman;
					AdminSettings setting = AdminSettings.get(1);
					render template: 'viewInvoice', model:[setting:setting, job:job, tradesmanInstance:tradesman, invoiceInstance:invoice]
				}
			}
		}
	}
	
	/**
	 * Create job for already available customer or creates job and sends
	 * registration link to selected email
	 * @param id - Tradesman Instance id that creates job for customer
	 */
	def createNewJobTradesman(Long id){
		log.info "createNewJobTradesman()"
		Tradesman tradesman = Tradesman.get(id);
		if(tradesman){
			log.info "params to create job : " + params;
			if(!StringUtils.isBlank(params.jobType)){
				try{
					Jobs job = new Jobs();
					job.setCustomerName(params.customerName);
					job.setCustomerEmail(params.customerEmail);
					job.setCustomerAddress1(params.addressLine1);
					job.setCustomerAddress2(params.addressLine2);
					job.setCustomerAddress3(params.addressLine3);
					job.setCustomerPostcode(params.postcode);
					
					job.setCreationDate(new Date());
					job.setTradesman(tradesman);
					job.setTradesmanCompanyName(tradesman?.companyName);
					job.setTradesmanEmail(tradesman?.companyEmail);
					
					job.setJobType(params.jobType);
					if(!StringUtils.isBlank(params.jobDate)){
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
						Date jobDate = sdf.parse(params.jobDate);
						job.setDateOfJob(jobDate);
					}
					job.setLaborHours(params.noOfHours?.toDouble());
					job.setLaborCost(params.costOfHours?.toDouble());
					job.setJobDescription(params.jobDesc);
					job.setStatus("PENDING");
					job.setInvoiceStatus("NO_INVOICE_CREATED");
					job.save(flush:true);
					if(params.count != null && params.count != "" && params.count?.toInteger() >= 0){
						for(int i=0; i < (params.count?.toInteger() + 1); i++){
							String partNameParam = "part_name"+i;
							String unitPriceParam = "unit_price"+i;
							String qtyParam = "qty"+i;
							
							if(params."$partNameParam" != "" && params."$partNameParam" != null && params."$unitPriceParam" && params."$qtyParam"){
								if(params."$unitPriceParam"?.toDouble() >= 0 && params."$qtyParam"?.toInteger() >= 0){
									log.info params."$partNameParam";
									log.info params."$unitPriceParam";
									log.info params."$qtyParam";
									Parts part = new Parts();
									part.setName(params."$partNameParam");
									part.setPricePerUnit(params."$unitPriceParam"?.toDouble());
									part.setQuantity(params."$qtyParam"?.toInteger());
									part.save(flush:true);
									job.addToParts(part);
									job.save(flush:true);
								}
							}
						}
					}
					redirect controller:'tradesman', action:'showJobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset]
				} catch(Exception e){
					log.info "Error while creating job for customer : " + e.message;
					e.printStackTrace();
					redirect controller:'tradesman', action:'showJobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset]
				}
			}
		} else{
			log.info "Tradesman not found with given id"
			flash.message = "Tradesman not found"
			redirect controller:'tradesman', action:'showJobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset]
		}
	}
}
