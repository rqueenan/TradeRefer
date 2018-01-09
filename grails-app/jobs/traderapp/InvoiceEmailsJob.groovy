package traderapp

import groovy.util.logging.Slf4j

@Slf4j
class InvoiceEmailsJob {
	def invoiceEmailsService;
    static triggers = {
     // simple repeatInterval: 5000l // execute job once in 5 seconds
	//	cron name: 'myTrigger', cronExpression: "0 0 0 * * ?" //At midnight
		cron name: 'myTrigger', cronExpression: "0 0/10 * 1/1 * ? *" //every 10 minute
    }
	
	def group = "MyGroup"
	def description = "Invoice EMail job with Cron Trigger"

    def execute() {
		log.info "Starting scheduler"
        // execute job
		invoiceEmailsService.updateEmails();
		invoiceEmailsService.updateTradesmanRegistrationEmail();
		invoiceEmailsService.updateUnpaidInvoices();
    }
}
