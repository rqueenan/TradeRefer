package traderapp

import java.text.SimpleDateFormat

import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovy.util.logging.Slf4j
import traderapp.model.AdminSettings
import traderapp.model.Customer
import traderapp.model.Emails
import traderapp.model.Invoice
import traderapp.model.Jobs

/**
 * Service contains all methods to send emails and track invoice emails
 */
@Transactional
@Slf4j
class InvoiceEmailsService {

	def mailService;
	def pdfRenderingService;
	def groovyPageRenderer;
	
	
    /**
     * Updates status for invoice and registration emails
     */
    def updateEmails() {
		log.info "updateEmails()"
		def unregisteredCustomers = Emails.findAllByCustomerRegisteredAndNotifiedToTradesman(false, false);
		if(unregisteredCustomers?.size() > 0){
			Date today = new Date();
			for(Emails email in unregisteredCustomers){
				log.info "Email : " + email;
				if(email?.followUpMailSent){
					log.info "Followup emal has been sent for invoice"
					Date followupMailSentDate = email?.getFollowUpMailDate();
					def duration_followup = getDiffInDays(followupMailSentDate, today);
					log.info "Followup duration : " + duration_followup;
					if(duration_followup >= 10){
						log.info "Followup duration is greater than 2 days.. Let's track the email";
						if(email?.followUpMailOpen){
							log.info "Followup email read by customer"
							if(email?.registrationLinkClickedFollowUpMail){
								log.info "Customer clicked on registration link in followup email"
								if(email?.customerRegistered){
									log.info "Notify tradesman about customer registration"
								} else{
									log.info "Notify tradesman about customer did not registered even after follow up mail";
									sendEmailToTradesman(email, "FOLLOW-UP");
									email?.setNotifiedToTradesman(true);
									email.save(flush:true);
								}
							} else{
								log.info "Notify tradesman that link in follow up mail is not clicked";
								sendEmailToTradesman(email, "FOLLOW-UP");
								email?.setNotifiedToTradesman(true);
								email.save(flush:true);
							}
						} else{
							log.info "Notify tradesman about followup email not opened";
							sendEmailToTradesman(email, "FOLLOW-UP");
							email?.setNotifiedToTradesman(true);
							email.save(flush:true);
						}
					}
				} else if(email?.emailTwoSent){
					log.info "Email two sent with invoice"
					Date mailSent2 = email?.getMailTwoSentDate();
					def duration2 = getDiffInDays(mailSent2, today);
					if(duration2 >= 10){
						log.info "Mail 2 duration is greater than 2 days.  Let's track 2nd email"
						if(email?.emailTwoOpen){
							log.info "Mail 2 is read by customer"
							if(email?.registrationLinkClickedSecondMail){
								log.info "Registration link in 2nd email is clicked"
								if(email?.customerRegistered){
									log.info "Notify tradesman that customer has registered";
								} else{
									log.info "Sending followup email for registration after email 2";
									def followupMailSent = sendMailWithRegistrationLink(email);
									if(followupMailSent){
										email?.setFollowUpMailSent(true);
										email?.setFollowUpMailDate(new Date());
										email.save(flush:true);
									} else{
										log.info "Failed to send follow up mail"
									}
								}
							} else{
								log.info "Sending followup email for registration after email 2";
								def followupMailSent = sendMailWithRegistrationLink(email);
								if(followupMailSent){
									email?.setFollowUpMailSent(true);
									email?.setFollowUpMailDate(new Date());
									email.save(flush:true);
								} else{
									log.info "Failed to send follow up mail"
								}
							}
						} else{
							log.info "Sending followup email for registration after email 2";
							def followupMailSent = sendMailWithRegistrationLink(email);
							if(followupMailSent){
								email?.setFollowUpMailSent(true);
								email?.setFollowUpMailDate(new Date());
								email.save(flush:true);
							} else{
								log.info "Failed to send follow up mail"
							}
						}
					}
				} else if(email?.emailOneSent){
					log.info "Email one is sent"
					Date mailSent1 = email?.getMailOneSentDate();
					def duration1 = getDiffInDays(mailSent1, today);
					if(duration1 >= 10){
						log.info "Email one duration is greater than 2 days. Lets's track 1st email"
						if(email?.emailOneOpen){
							log.info "Customer read his 1st mail"
							if(email?.registrationLinkClickedFirstMail){
								log.info "Customer clicked on registration link in 1st mail"
								if(email?.customerRegistered){
									log.info "Notify tradesman that customer has registered";
								} else{
									log.info "sending email 2";
									def mailTwoSent = sendMailWithRegistrationLink(email);
									if(mailTwoSent){
										email?.setEmailTwoSent(true);
										email?.setMailTwoSentDate(new Date());
										email.save(flush:true);
									} else{
										log.info "failed to send mail2";
									}
								}
							} else{
								log.info "sending email 2";
								def mailTwoSent = sendMailWithRegistrationLink(email);
								if(mailTwoSent){
									email?.setEmailTwoSent(true);
									email?.setMailTwoSentDate(new Date());
									email.save(flush:true);
								} else{
									log.info "failed to send mail2";
								}
							}
						} else{
							log.info "sending email 2";
							def mailTwoSent = sendMailWithRegistrationLink(email);
							if(mailTwoSent){
								email?.setEmailTwoSent(true);
								email?.setMailTwoSentDate(new Date());
								email.save(flush:true);
							} else{
								log.info "failed to send mail2";
							}
						}
					}
				}
			}
		}
    }
	
	
	/**
	 * @param date1 - Mail sent date
	 * @param date2 - Today's date
	 * @return difference in dates in terms of minutes 
	 */
	def getDiffInDays(Date date1, Date date2){
		log.info "getDiffInDays()"
		use(TimeCategory){
			def duration = date2 - date1;
			log.info "duration: "+duration?.minutes;
			return duration.minutes;
		}
	}
	
	/**
	 * Mails registration link to customer
	 * @param email - Emails Instance
	 * @return boolean value. True in case of mail sent successfully
	 */
	def sendMailWithRegistrationLink(Emails email){
		log.info "sendMailWithRegistrationLink()"
		if(email){
			Invoice invoice = Invoice.findByInvoiceNumber(email?.invoiceNumber);
			AdminSettings setting = AdminSettings.get(1);
			if(invoice){
				Jobs job = Jobs.findByInvoice(invoice);
				if(job){
					try{
						String fileName = invoice?.invoiceNumber+".pdf";
						ByteArrayOutputStream bytes = pdfRenderingService.render(template: "/invoice/invoice", model: [setting:setting, invoice: invoice, job:job, tradesmanCompanyName:job?.tradesman?.companyName, tradesmanInstance:job?.tradesman], filename: invoice?.invoiceNumber+".pdf");
						def content = groovyPageRenderer.render(view: '/invoice/invoiceEmailOne', model:[tradesmanCompanyName:job?.tradesman?.companyName, name:invoice?.customerName, customerEmail:invoice?.customerEmail, invoiceNumber:invoice?.invoiceNumber, jobType:job?.jobType, jobDesc: job?.jobDescription])
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
						mailService.sendMail{
							multipart true
							to invoice?.customerEmail
							from job?.tradesman?.companyEmail
							subject "TradeRefer : Invoice due for job completed by "+ job?.tradesman?.companyName + " on " + sdf.format(job?.dateOfJob)
							attachBytes fileName, "application/pdf", bytes.toByteArray()
							html(content)
						}
						log.info "Email sent to customer with invoice";
						return true;
					}catch(Exception e){
						log.info "Error while sending invoice email";
						e.printStackTrace();
						return false;
					}
				}
			}
		}
	}
	
	/**
	 * Sends email to tradesman if invoice is overdue
	 * @param email Emails Instance
	 * @param type
	 */
	def sendEmailToTradesman(Emails email, String type){
		log.info "sendEmailToTradesman()"
		if(email){
			Invoice invoice = Invoice.findByInvoiceNumber(email?.invoiceNumber);
			if(invoice){
				Jobs job = Jobs.findByInvoice(invoice);
				if(job){
					try{
						def content = groovyPageRenderer.render(view: '/invoice/firstInvoiceUnpaidTradesman', model:[customer_name:invoice?.customerName, company_contact_name:job?.tradesman?.name])
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
						mailService.sendMail{
							to job?.tradesman?.companyEmail
							subject "TradeRefer : Invoice overdue for job completed for "+ invoice?.customerName + " on " + sdf.format(job?.dateOfJob)
							html(content)
						}
						log.info "Notification Email sent to tradesman for unpaid first invoice";
					} catch(Exception e){
						log.info "Failed to send notification email to tradesman"
						e.printStackTrace();
					}
				}
			}			
		}
	}
	
	/**
	 * Sends job creation email to tradesman if tradesman is not registered 
	 * @param tradesmanName - Tradesman's company name
	 * @param tradesmanEmail - Tradesman contact mail address
	 * @param job - Jobs Instance
	 * @return boolean true if mail sent successfully
	 */
	def sendJobCreationMailToTradesman_first(String tradesmanName, String tradesmanEmail, Jobs job){
		log.info "sendJobCreationMailToTradesman_first()"
		if(!job?.jobCreationMailSent_one){
			try{
				def content = groovyPageRenderer.render(view: '/jobs/jobCreationMail', model:[companyName:tradesmanName, companyEmail:tradesmanEmail, customerName:job?.customerName, jobId:job?.id])
				mailService.sendMail{
					to tradesmanEmail
					subject tradesmanName + ", " +job?.customerName+" wants to use TradeRefer to pay for your work."
					html(content)
				}
				log.info "Notification Email sent to tradesman for job creation";
				job.setJobCreationMailSent_one(true);
				job.setMailOne(new Date());
				job.save(flush:true);
				return true;
			} catch(Exception e){
				log.info "Failed to send job creation mail to tradesman"
				e.printStackTrace();
				return false;
			}
		}
	}
	
	/**
	 * Notifies customer about tradesman registration on TradeRefer
	 * @param tradesmanName - Tradesman's company name
	 * @param tradesmanEmail - Tradesman contact email address
	 * @param job - Jobs Instance
	 */
	def notifyCustomerAboutTradesmanRegistration(String tradesmanName, String tradesmanEmail, Jobs job){
		log.info "notifyCustomerAboutTradesmanRegistration()"
		if(!job?.tradesman && job?.jobCreationMailSent_one && job?.jobCreationMailSent_two && job?.jobCreationMailSent_three){
			try{
				def content = groovyPageRenderer.render(view: '/jobs/notifyCustomer', model:[companyName:tradesmanName, customerName:job?.customerName])
				mailService.sendMail{
					to job?.customerEmail
					subject job?.customerName+", " +tradesmanName+ " has not yet registered on TradeRefer"
					html(content)
				}
				log.info "Notification Email sent to customer for tradesman not registered yet";
				job.setJobCreationMailSent_notify_customer(true);
				job.save(flush:true);
			} catch(Exception e){
				log.info "Failed to notify customer about tradesman regisration not completed"
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sends follow up emails for tradesman registartion
	 * @param tradesmanName - Tradesman's company name
	 * @param tradesmanEmail - Tradesman's contact email address
	 * @param job - Jobs instance
	 * @param type - Mail type
	 */
	def sendRegistrationMailToTradesman(String tradesmanName, String tradesmanEmail, Jobs job, String type){
		log.info "sendRegistrationMailToTradesman()"
		
		if(type == "TWO"){
			def content = groovyPageRenderer.render(view: '/jobs/jobCreationMail', model:[companyName:tradesmanName, companyEmail:tradesmanEmail, customerName:job?.customerName, jobId:job?.id])
			mailService.sendMail{
				to tradesmanEmail
				subject tradesmanName + ", Join us at TradeRefer and change the way you run your business"
				html(content)
			}
			log.info "Second job creation mail sent for the tradesman $tradesmanEmail";
			job.setJobCreationMailSent_two(true);
			job.setMailTwo(new Date());
			job.save(flush:true);
		} else if(type == "THREE"){
			def content = groovyPageRenderer.render(view: '/jobs/jobCreationMail_Third', model:[companyName:tradesmanName, companyEmail:tradesmanEmail, customerName:job?.customerName, jobId:job?.id])
			mailService.sendMail{
				to tradesmanEmail
				subject tradesmanName + ", Join us at TradeRefer and change the way you run your business"
				html(content)
			}
			log.info "Third job creation mail sent for the tradesman $tradesmanEmail";
			job.setJobCreationMailSent_three(true);
			job.setMailThree(new Date());
			job.save(flush:true);
		}
	}
	
	/**
	 * Update and follow up for tradesman registration mails
	 */
	def updateTradesmanRegistrationEmail(){
		log.info "updateTradesmanRegistrationEmail()"
		def jobs = Jobs.findAllByTradesmanAndJobCreationMailSent_notify_customer(null,false);
		if(jobs?.size() > 0){
			Date today = new Date();
			for(Jobs job : jobs){
				if(job?.jobCreationMailSent_one){
					log.info "First mail of job creation is sent to tradesman";
					Date mailOneSentDate = job?.mailOne;
					def duration_one = getDiffInDays(mailOneSentDate, today);
					if(duration_one >= 10){
						if(job?.jobCreationMailSent_two){
							Date mailTwoSentDate = job?.mailTwo;
							def duration_two = getDiffInDays(mailTwoSentDate, today);
							if(duration_two >= 10){
								if(job?.jobCreationMailSent_three){
									Date mailThreeSendDate = job?.mailThree;
									def duration_three = getDiffInDays(mailThreeSendDate, today);
									if(duration_three >= 10){
										log.info "It's been more than 2 days we sent the 3rd mail. Let's notify customer.";
										notifyCustomerAboutTradesmanRegistration(job?.tradesmanCompanyName, job?.tradesmanEmail, job);
									} else{
										log.info "It is not yet 2 days we sent the third mail. Let's wait for some more time."
									}
								} else {
									log.info "It's been more than 2 days we sent the 2nd mail. Let's send another.";
									sendRegistrationMailToTradesman(job?.tradesmanCompanyName, job?.tradesmanEmail, job, "THREE");
								}
							} else{
								log.info "It is not yet 2 days we sent the second mail. Let's wait for some more time."
							}
						} else{
							log.info "It's been more than 2 days we sent the 1st mail. Let's send another.";
							sendRegistrationMailToTradesman(job?.tradesmanCompanyName, job?.tradesmanEmail, job, "TWO");
						}
					} else{
						log.info "It is not yet 2 days we sent the first mail. Let's wait for some more time."
					}
				} else {
					log.info "First email of Job creation is not yet sen too tradesman. Sending first mail.."
					sendJobCreationMailToTradesman_first(job?.tradesmanCompanyName, job?.tradesmanEmail, job);
				}
			}
		}
	}
	
	/**
	 * Sends payment transaction notification to tradesman
	 * @param job Jobs Instance
	 * @param invoice Invoice Instance
	 */
	def sendPaymentEmailToTradesman(Jobs job, Invoice invoice){
		if(job){
			def content = groovyPageRenderer.render(view: '/jobs/paymentNotification', model:[companyName:job?.tradesman?.companyName, customerName:job?.customerName, inoiceNumber:invoice?.invoiceNumber])
			mailService.sendMail{
				to job?.tradesman?.companyEmail
				subject job?.tradesman?.companyName + ", " +job?.customerName+ " has just paid your invoice."
				html(content)
			}
		}
	}
	
	def sendOverdueNotificationToCustomer(Emails email){
		if(email){
			Invoice invoice = Invoice.findByInvoiceNumber(email?.invoiceNumber);
			Jobs job = Jobs.findByInvoice(invoice);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			def content = groovyPageRenderer.render(view: '/jobs/overdueNotification', model:[companyName:job?.tradesman?.companyName, customerName:job?.customerName, inoiceNumber:invoice?.invoiceNumber, customeEmail: invoice?.customerEmail])
			if(invoice && job){
				mailService.sendMail{
					to job?.tradesman?.companyEmail
					subject "TradeRefer : Invoice overdue for job completed by "+job?.tradesman?.companyName+" on "+ sdf.format(job?.dateOfJob)
					html(content)
				}
			}
		}
	}
	
	def updateUnpaidInvoices(){
		List overdueInvoices = Invoice.findAllByStatus("SENT");
		if(overdueInvoices){
			for(Invoice invoice in overdueInvoices){
				Emails email = Emails.findByInvoiceNumber(invoice?.invoiceNumber);
				if(email){
					Date invoiceSentDate = email?.mailOneSentDate;
					Date today = new Date();
					def duration = getDiffInDays(invoiceSentDate, today);
					if(duration >= 40){
						log.info "It has been 5 days since invoice mail is sent but invoice is still unpaid. Marking it as Overdue.";
						invoice.setStatus("OVERDUE");
						invoice.save(flush:true);
						try{
							sendOverdueNotificationToCustomer(email);
						} catch(Exception e){
							e.printStackTrace();
							log.error("Failed to send overdue notification email to customer");
						}
					} else{
						log.info "Not yet 5 days since the mail sent with invoice"
					}
				}
			}
		}
	}
}
