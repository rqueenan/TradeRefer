package traderapp.model

import static org.springframework.http.HttpStatus.*

import java.lang.ProcessBuilder.Redirect

import grails.transaction.Transactional
import groovy.util.logging.Slf4j
import traderapp.user.User

/**
 * Contains methods that shows list of invoices
 * to tradesman and mails invoice to customer.
 */
@Transactional(readOnly = true)
@Slf4j
class InvoiceController {
	def springSecurityService;
	def mailService;
	
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * Redirects tradesman to the list of invoices
     */
    def index(Integer max) {
		User user = springSecurityService.currentUser;
		log.info "index() | user : " +user;
		params.max = Math.min(max ?: 10, 100);
		if(user){
			Tradesman loggedInTradesman = Tradesman.findByUser(user);
			log.info "logged in tradesman : " + loggedInTradesman;
			log.info "work types in logged in tradesman : " + loggedInTradesman.workType;
			def invoice_count = Invoice.findAllByTradesmanEmail(loggedInTradesman?.companyEmail).size();
			def invoices = Invoice.findAllByTradesmanEmail(loggedInTradesman?.companyEmail, [max:params.max, sort:params.sort, order:params.order, offset:params.offset]);
			[tradesmanInstance:loggedInTradesman, invoices:invoices, invoice_count:invoice_count]
		}
    }

    def show(Invoice invoice) {
        respond invoice
    }

    def create() {
        respond new Invoice(params)
    }

    @Transactional
    def save(Invoice invoice) {
        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (invoice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond invoice.errors, view:'create'
            return
        }

        invoice.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect invoice
            }
            '*' { respond invoice, [status: CREATED] }
        }
    }

    def edit(Invoice invoice) {
        respond invoice
    }

    @Transactional
    def update(Invoice invoice) {
        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (invoice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond invoice.errors, view:'edit'
            return
        }

        invoice.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect invoice
            }
            '*'{ respond invoice, [status: OK] }
        }
    }

    @Transactional
    def delete(Invoice invoice) {

        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        invoice.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	/**
	 * Sends mail to customer with attachment of PDF Invoice
	 * and redirects back to list of jobs.
	 * @param id - InvoiceInstance id
	 */
	def sendInvoice(Long id){
		log.info "sendInvoice()"; 
		Invoice invoice = Invoice.get(id);
		Jobs job = Jobs.findByInvoice(invoice);
		AdminSettings setting = AdminSettings.get(1);
		if(invoice){
			try{
				Emails email = new Emails();
				email.setCustomerEmail(invoice?.customerEmail);
				email.setTradesmanEmail(job?.tradesman?.companyEmail);
				email.setInvoiceNumber(invoice?.invoiceNumber);
				email.save(flush:true);
				job.setInvoiceStatus("INVOICE_SENT");
				job.save(flush:true);
				log.info "Email sent to customer with invoice";
				if(Customer?.findByEmail(invoice?.customerEmail)){
					email.setCustomerRegistered(true);
					email.save(flush:true);
					String fileName = invoice?.invoiceNumber+".pdf";
					ByteArrayOutputStream bytes = pdfRenderingService.render(template: "/invoice/invoice", model: [setting:setting, invoice: invoice, job:job, tradesmanCompanyName:job?.tradesman?.companyName, tradesmanInstance:job?.tradesman], filename: invoice?.invoiceNumber+".pdf");
					mailService.sendMail{
						multipart true
						to invoice?.customerEmail
						subject  job?.customerName +" : Invoice due for "+job?.tradesman?.companyName
						attachBytes fileName, "application/pdf", bytes.toByteArray()
						body(view: 'invoiceEmailRegistered', model:[tradesmanCompanyName:job?.tradesman?.companyName, name:invoice?.customerName, customerEmail:invoice?.customerEmail, invoiceNumber:invoice?.invoiceNumber, jobType:job?.jobType, jobDesc: job?.jobDescription])
					}
				} else if(FacebookUser?.findByEmail(invoice?.customerEmail)){
					email.setCustomerRegistered(true);
					email.save(flush:true);
					String fileName = invoice?.invoiceNumber+".pdf";
					ByteArrayOutputStream bytes = pdfRenderingService.render(template: "/invoice/invoice", model: [setting:setting, invoice: invoice, job:job, tradesmanCompanyName:job?.tradesman?.companyName, tradesmanInstance:job?.tradesman], filename: invoice?.invoiceNumber+".pdf");
					mailService.sendMail{
						multipart true
						to invoice?.customerEmail
						subject  job?.customerName +" : Invoice due for "+job?.tradesman?.companyName
						attachBytes fileName, "application/pdf", bytes.toByteArray()
						body(view: 'invoiceEmailRegistered', model:[tradesmanCompanyName:job?.tradesman?.companyName, name:invoice?.customerName, customerEmail:invoice?.customerEmail, invoiceNumber:invoice?.invoiceNumber, jobType:job?.jobType, jobDesc: job?.jobDescription])
					}
				} else if(LinkedInUser?.findByEmail(invoice?.customerEmail)){
					email.setCustomerRegistered(true);
					email.save(flush:true);
					String fileName = invoice?.invoiceNumber+".pdf";
					ByteArrayOutputStream bytes = pdfRenderingService.render(template: "/invoice/invoice", model: [setting:setting, invoice: invoice, job:job, tradesmanCompanyName:job?.tradesman?.companyName, tradesmanInstance:job?.tradesman], filename: invoice?.invoiceNumber+".pdf");
					mailService.sendMail{
						multipart true
						to invoice?.customerEmail
						subject  job?.customerName +" : Invoice due for "+job?.tradesman?.companyName
						attachBytes fileName, "application/pdf", bytes.toByteArray()
						body(view: 'invoiceEmailRegistered', model:[tradesmanCompanyName:job?.tradesman?.companyName, name:invoice?.customerName, customerEmail:invoice?.customerEmail, invoiceNumber:invoice?.invoiceNumber, jobType:job?.jobType, jobDesc: job?.jobDescription])
					}
				} else{
					String fileName = invoice?.invoiceNumber+".pdf";
					ByteArrayOutputStream bytes = pdfRenderingService.render(template: "/invoice/invoice", model: [setting:setting, invoice: invoice, job:job, tradesmanCompanyName:job?.tradesman?.companyName, tradesmanInstance:job?.tradesman], filename: invoice?.invoiceNumber+".pdf");
					mailService.sendMail{
						multipart true
						to invoice?.customerEmail
						subject  job?.customerName +" : Invoice due for "+job?.tradesman?.companyName
						attachBytes fileName, "application/pdf", bytes.toByteArray()
						body(view: 'invoiceEmailOne', model:[tradesmanCompanyName:job?.tradesman?.companyName, name:invoice?.customerName, customerEmail:invoice?.customerEmail, invoiceNumber:invoice?.invoiceNumber, jobType:job?.jobType, jobDesc: job?.jobDescription])
					}
				}
				invoice.setStatus("SENT");
				invoice.save(flush:true);
			}catch(Exception e){
				log.info "Error while sending invoice email : "+e.message;
				e.printStackTrace();
			}
		}
		redirect controller:'tradesman', action:'showJobs';
	}
}
