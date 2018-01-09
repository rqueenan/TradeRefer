package traderapp.model

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import groovy.util.logging.Slf4j

import java.lang.invoke.InfoFromMemberName
import java.net.URLPermission.Authority
import java.text.SimpleDateFormat

import org.apache.commons.lang.StringUtils

import traderapp.user.Role
import traderapp.user.User
import traderapp.user.UserRole

/**
 * Contains methods that create and update 
 * jobs for tradesman and customers both,
 * create and update invoice as well as send feedback to tradesman
 */
@Transactional(readOnly = true)
@Slf4j
class JobsController {
	def mailService;
	def springSecurityService;
	def invoiceEmailsService;
	def groovyPageRenderer;
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Jobs.list(params), model:[jobsCount: Jobs.count()]
    }

    def show(Jobs jobs) {
        respond jobs
    }

    def create() {
        respond new Jobs(params)
    }

    @Transactional
    def save(Jobs jobs) {
        if (jobs == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (jobs.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond jobs.errors, view:'create'
            return
        }

        jobs.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'jobs.label', default: 'Jobs'), jobs.id])
                redirect jobs
            }
            '*' { respond jobs, [status: CREATED] }
        }
    }

    def edit(Jobs jobs) {
        respond jobs
    }

    @Transactional
    def update(Jobs jobs) {
        if (jobs == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (jobs.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond jobs.errors, view:'edit'
            return
        }

        jobs.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'jobs.label', default: 'Jobs'), jobs.id])
                redirect jobs
            }
            '*'{ respond jobs, [status: OK] }
        }
    }

    @Transactional
    def delete(Jobs jobs) {

        if (jobs == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        jobs.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'jobs.label', default: 'Jobs'), jobs.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'jobs.label', default: 'Jobs'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	/**
	 * Create Job for tradesman from already available tradesman
	 * or sends registration link to tradesman if not available in database.
	 * Also, sedns job creation mail to tradesman
	 * @param id - Customer Instance id that creates job for tradesman
	 */
	def createNewJobCustomer(Long id){
		log.info "createNewJobCustomer()";
		String customerName = "";
		String customerEmail = "";
		User user = springSecurityService.currentUser;
		UserRole user_role = UserRole.findByUser(user);
		log.info "role : " + user_role?.role?.authority;
		if(user_role.role.authority == "ROLE_CUSTOMER"){
			Customer customer = Customer.get(id);
			customerName = customer.name;
			customerEmail = customer.email;
			customer.setAddressLine1(params.addressLine1);
			customer.setAddressLine2(params.addressLine2);
			customer.setAddressLine3(params.addressLine3);
			customer.setPostcode(params.postcode);
			customer.save(flush:true);
		} else if(user_role.role.authority == "ROLE_FACEBOOK_CUSTOMER"){
			FacebookUser fbUser = FacebookUser.get(id);
			customerName = fbUser.name;
			customerEmail = fbUser.email;
			fbUser.setAddressLine1(params.addressLine1);
			fbUser.setAddressLine2(params.addressLine2);
			fbUser.setAddressLine3(params.addressLine3);
			fbUser.setPostcode(params.postcode);
			fbUser.save(flush:true);
		} else if(user_role.role.authority == "ROLE_LINKEDIN_CUSTOMER"){
			LinkedInUser liUser = LinkedInUser.get(id);
			customerName = liUser.name;
			customerEmail = liUser.email;
			liUser.setAddressLine1(params.addressLine1);
			liUser.setAddressLine2(params.addressLine2);
			liUser.setAddressLine3(params.addressLine3);
			liUser.setPostcode(params.postcode);
			liUser.save(flush:true);
		}
		log.info customerName + " ==== " + customerEmail;
		if(customerName && customerEmail){
			log.info "params to create job : " + params;
			if(!StringUtils.isBlank(params.jobType)){
				try{
					Jobs job = new Jobs();
					job.setCustomerName(customerName);
					job.setCustomerEmail(customerEmail);
					job.setCustomerAddress1(params.addressLine1);
					job.setCustomerAddress2(params.addressLine2);
					job.setCustomerAddress3(params.addressLine3);
					job.setCustomerPostcode(params.postcode);
					job.setCreationDate(new Date());
					if(!StringUtils.isBlank(params.jobDate)){
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
						Date jobDate = sdf.parse(params.jobDate);
						job.setDateOfJob(jobDate);
					}
					Tradesman tradesman = Tradesman.findByCompanyEmail(params.tradesmanEmail);
					if(tradesman){
						job.setTradesman(tradesman);
						job.setTradesmanCompanyName(tradesman?.companyName);
						job.setTradesmanEmail(tradesman?.companyEmail);
					} else{
						job.setTradesmanEmail(params.tradesmanEmail);
						job.setTradesmanCompanyName(params.tradesmanCompanyName);
					}
					job.setJobType(params.jobType);
					job.setJobDescription(params.jobDesc);
					job.setStatus("PENDING");
					job.setInvoiceStatus("NO_INVOICE_CREATED");
					job.save(flush:true);
					
					if(!job?.tradesman){
						try{
							log.info "Tradesman is not registered for this job. Sending Job Creation mail";
							invoiceEmailsService.sendJobCreationMailToTradesman_first(job?.tradesmanCompanyName, job?.tradesmanEmail, job);
						} catch(Exception ex){
							log.info "Error occurred while sending job creation mail to tradesman : " + ex.message;
							ex.printStackTrace();
						}
					} else{
						try{
							def content = groovyPageRenderer.render(view: '/jobs/jobCreationMail_registered', model:[companyName:job?.tradesman?.companyName, companyEmail:job?.tradesman?.companyEmail, customerName:job?.customerName, jobId:job?.id])
							log.info "content : " + content;
							mailService.sendMail{
								to job?.tradesman?.companyEmail
								subject job?.tradesman?.companyName+ ", " +job?.customerName+" wants to use TradeRefer to pay for your work."
								html(content)
							}
							log.info "Notification Email sent to tradesman for job creation";
						} catch(Exception e_mail){
							log.info "Failed to send job creation mail to tradesman"
							e_mail.printStackTrace();
						}
					}
					
				} catch(Exception e){
					log.info "Error occurred while creating job for tradesman : " + e.message;
					e.printStackTrace();
				}
				redirect controller:'profile', action:'jobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset]
			}
		}
	}
	
	
	
	/**
	 * Edit and Update Job Instance
	 * @param id - Job Instance id
	 */
	def updateJobTradesman(Long id){
		log.info "updateJobTradesman() | params : " + params;
		try{
			Jobs job = Jobs.get(id);
			job.setCustomerName(params.customerName);
			job.setCustomerEmail(params.customerEmail);
			job.setCustomerAddress1(params.addressLine1);
			job.setCustomerAddress2(params.addressLine2);
			job.setCustomerAddress3(params.addressLine3);
			job.setCustomerPostcode(params.postcode);
			job.setJobType(params.jobType);
			if(!StringUtils.isBlank(params.jobDate_edit)){
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
				Date jobDate = sdf.parse(params.jobDate_edit);
				job.setDateOfJob(jobDate);
			}
			job.setLaborHours(params.noOfHours?.toDouble());
			job.setLaborCost(params.costOfHours?.toDouble());
			job.setJobDescription(params.jobDesc);
			if(job.invoiceStatus == null || job.invoiceStatus == ""){
				job.setInvoiceStatus("NO_INVOICE_CREATED");
			}
			job.parts = null;
			job.save(flush:true);
			if(params.count != null && params.count != "" && params.count?.toInteger() >= 0){
				for(int i=0; i < (params.count?.toInteger() + 1); i++){
					String partNameParam = "part_name"+i;
					String unitPriceParam = "unit_price"+i;
					String qtyParam = "qty"+i;
					
					if(params."$partNameParam" != "" && params."$partNameParam" != null && params."$unitPriceParam" && params."$qtyParam"){
						if(params."$unitPriceParam"?.toDouble() >= 0 && params."$qtyParam"?.toInteger() >= 0){
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
			if(job?.invoice){
				Invoice invoice = job?.invoice;
				double totalPrice = 0;
				totalPrice = job?.laborCost;
				if(job?.parts){
					for(int i=0; i < job?.parts?.size(); i++){
						Parts part = job?.parts[i];
						totalPrice += (part?.pricePerUnit * part?.quantity);
					}
				}
				
				if(invoice?.discount > 0){
					totalPrice = totalPrice - invoice?.discount;
				}
				
				invoice.setTotalPrice(totalPrice);
				invoice.setLaborCost(job?.laborCost);
				invoice.setLaborHours(job?.laborHours);
				invoice.save(flush:true);
				
			}
			
		} catch(Exception e){
			log.info "Error while updating job instance : " + e.message;
			e.printStackTrace();
		}
		redirect controller:'tradesman', action:'showJobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset]
	}
	
	/**
	 * Create invoice instance
	 * @param id - Jobs Instance id
	 */
	def updateInvoice(Long id){
		log.info "updateInvoice() | params : $params";
		try {
			Jobs job = Jobs.get(id);
			Invoice invoice = job?.invoice;
			log.info "invoice : " + invoice;
			if(invoice == null){
				invoice = new Invoice();
				invoice.setCustomerName(job?.customerName);
				invoice.setCustomerEmail(job?.customerEmail);
				invoice.setCustomerAddress1(job?.customerAddress1);
				invoice.setCustomerAddress2(job?.customerAddress2);
				invoice.setCustomerAddress3(job?.customerAddress3);
				invoice.setCustomerPostcode(job?.customerPostcode);
				invoice.setTradesmanName(job?.tradesman?.name);
				invoice.setTradesmanEmail(job?.tradesman?.companyEmail);
				invoice.setTradesmanPhone(job?.tradesman?.phone);
				invoice.setTradesmanAddress(job?.tradesman?.addressLine1 + " " + job?.tradesman?.addressLine2 + " " + job?.tradesman?.addressLine3 + " " + job?.tradesman?.postcode);
				if(job?.dateOfJob != null && job?.dateOfJob != ""){
					invoice.setJobDate(job?.dateOfJob);
				}				
				invoice.setJobDesc(job?.jobDescription);
				invoice.setJobType(job?.jobType);
				if(params.laborHours_invoice){
					invoice.setLaborHours(params.laborHours_invoice?.toDouble());
				}
				if(params.laborCost_invoice){
					invoice.setLaborCost(params.laborCost_invoice?.toDouble());
				}
				invoice.setDiscount(params.discount?.toDouble());
				invoice.setTotalPrice(params.total?.toDouble());
				invoice.setAfterVAT(params.total?.toDouble());
				invoice.setStatus("PENDING");
				Date newDate = new Date();
				invoice.setInvoiceNumber("INV-"+newDate?.getTime()?.toString());
				invoice.save(flush:true);
				
				if(params.laborHours_invoice){
					job.setLaborHours(params.laborHours_invoice?.toDouble());
				}
				if(params.laborCost_invoice){
					job.setLaborCost(params.laborCost_invoice?.toDouble());
				}
				job.setInvoice(invoice);
				job.setInvoiceStatus("INVOICE_CREATED");
				job.setStatus("COMPLETE");
				job.parts = null;
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
			}
		} catch(Exception e){
			log.info "Error while creating Invoice instance : " + e.message;
			e.printStackTrace();
		}
		
		redirect controller:'tradesman', action:'showJobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset];
	}
	
	/**
	 * Edit and update invoice instance
	 * @param id - Invoice Instance Id
	 */
	def editInvoice(Long id){
		log.info "editInvoice() | params : $params";
		if(id){
			try {
				Invoice invoice = Invoice.get(id);
				log.info "invoice : " + invoice;
				if(invoice){
					invoice.setDiscount(params.discount?.toDouble());
					invoice.setTotalPrice(params.total?.toDouble());
					if(params.laborHours_invoice){
						invoice.setLaborHours(params.laborHours_invoice?.toDouble());
					}
					if(params.laborCost_invoice){
						invoice.setLaborCost(params.laborCost_invoice?.toDouble());
					}
					invoice.save(flush:true);
					
					Jobs job = Jobs.findByInvoice(invoice);
					if(params.laborHours_invoice){
						job.setLaborHours(params.laborHours_invoice?.toDouble());
					}
					if(params.laborCost_invoice){
						job.setLaborCost(params.laborCost_invoice?.toDouble());
					}
					job.parts = null;
					job.save(flush:true);
					
					if(params.count != null && params.count != "" && params.count?.toInteger() >= 0){
						for(int i=0; i < (params.count?.toInteger() + 1); i++){
							String partNameParam = "part_name"+i;
							String unitPriceParam = "unit_price"+i;
							String qtyParam = "qty"+i;
							
							if(params."$partNameParam" != "" && params."$partNameParam" != null && params."$unitPriceParam" && params."$qtyParam"){
								if(params."$unitPriceParam"?.toDouble() >= 0 && params."$qtyParam"?.toInteger() >= 0){
									Parts part = new Parts();
									part.setName(params."$partNameParam");
									part.setPricePerUnit(params."$unitPriceParam"?.toDouble());
									part.setQuantity(params."$qtyParam"?.toInteger());
									part.save(flush:true);
									log.info "Part to save : " + part;
									job.addToParts(part);
									job.save(flush:true);
								}
							}
						}
					}
				}
				
			} catch(Exception e){
				log.info "Error while updating invoice instance : " + e.message;
				e.printStackTrace();
			}
		}
		redirect controller:'tradesman', action:'showJobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset];
	}
	
	/**
	 * Render template to view feedback for tradesman
	 * @param jobId - Jobs instance id
	 */
	def viewFeedbackForm(Long jobId){
		log.info "viewFeedbackForm()"
		if(jobId){
			Jobs job = Jobs.get(jobId);
			render template:'viewFeedback', model:[job:job, max:params.max, sort:params.sort, order:params.order, offset:params.offset];
		}
	}
	
	/**
	 * Create feedback for tradesman
	 * @param id - Jobs instance id
	 */
	def saveFeedback(Long id){
		log.info "saveFeedback() | params for feedback : $params";
		Jobs job = Jobs.get(id);
		if(job){
			try{
				if(params.feedbackCount){
					Feedbacks feedback = new Feedbacks();
					feedback.setStarCounts(params.feedbackCount?.toDouble());
					if(params.comment){
						feedback.setComment(params.comment);
					}
					feedback.save(flush:true);
					job.setFeedback(feedback);
					job.save(flush:true);
				}
				mailService.sendMail{
					to job?.tradesman?.companyEmail
					subject job?.tradesman?.companyName +", "+ job?.customerName +" has just provided feedback on a recently completed job."
					body(view: 'feedbackEmail', model:[customerName:job?.customerName, jobDesc:job?.jobDescription, companyName:job?.tradesman?.companyName])
				}
			} catch(Exception e){
				log.info "Error while creating and sending feedback to tradesman : " + e.message;
				e.printStackTrace();
			}
		}
		redirect controller:'profile', action:'jobs', params:[max:params.max, sort:params.sort, order:params.order, offset:params.offset];
	}
	
	def validateNotTradesman(String email){
		if(email){
			def tradesmans = Tradesman.findAllByCompanyEmail(email);
			if(tradesmans?.size() > 0){
				render true;
			} else{
				render false;
			}
		}
	}
	
	def validateNotCustomer(String email){
		List availabelCustomers = new ArrayList();
		def customers = Customer.findAllByEmail(email);
		def fbUsers = FacebookUser.findAllByEmail(email);
		def liUsers = LinkedInUser.findAllByEmail(email);
		if(customers?.size() > 0 || fbUsers?.size() > 0 || liUsers?.size() > 0){
			render true;
		} else{
			render false;
		}
	}
}
