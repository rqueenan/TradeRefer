package traderapp.model

import grails.databinding.BindingFormat

class Jobs {
	static hasMany = [parts:Parts];
	String customerName;
	String customerEmail;
	String customerAddress1;
	String customerAddress2;
	String customerAddress3;
	String customerPostcode;
	String tradesmanEmail;
	String tradesmanCompanyName;
	
	@BindingFormat('dd MMM yyyy')
	Date dateOfJob;
	
	Date creationDate;
	String jobType;
	Double laborHours;
	Double laborCost;
	String jobDescription;
	Tradesman tradesman;
	String status;
	String invoiceStatus;
	Invoice invoice;
	Feedbacks feedback;
	boolean jobCreationMailSent_one = false;
	boolean jobCreationMailSent_two = false;
	boolean jobCreationMailSent_three = false;
	boolean jobCreationMailSent_notify_customer = false;
	Date mailOne;
	Date mailTwo;
	Date mailThree;
	boolean jobCreationMail_one_read = false;
	boolean jobCreationMail_two_read = false;
	boolean jobCreationMail_three_read = false;
	boolean registrationLinkClicked = false;
	
    static constraints = {
		status nullable:true, blank:true, inList:["PENDING", "COMPLETE", "PAID"];
		laborHours nullable:true, blank:true;
		laborCost nullable:true, blank:true;
		jobDescription nullable:true, blank:true;
		dateOfJob nullable:true, blank:true;
		customerName nullable:true, blank:true;
		customerEmail nullable:true, blank:true;
		customerAddress1 nullable:true, blank:true;
		customerAddress2 nullable:true, blank:true;
		customerAddress3 nullable:true, blank:true;
		customerPostcode nullable:true, blank:true;
		invoice nullable:true; blank:true;
		invoiceStatus inList:["NO_INVOICE_CREATED", "INVOICE_CREATED", "INVOICE_SENT", "INVOICE_PAID"];
		feedback nullable:true, blank:true;
		tradesman nullable:true, blank:true;
		mailOne nullable:true, blank:true;
		mailTwo nullable:true, blank:true;
		mailThree nullable:true, blank:true;
    }
}
