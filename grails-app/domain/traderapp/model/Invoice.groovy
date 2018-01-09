package traderapp.model

import java.lang.invoke.DirectMethodHandle.StaticAccessor

class Invoice {
	static hasMany = [parts:Parts];
	
	String customerName;
	String customerAddress;
	String customerAddress1;
	String customerAddress2;
	String customerAddress3;
	String customerPostcode;
	String customerEmail;
	String customerPhone;
	String tradesmanName;
	String tradesmanEmail;
	String tradesmanPhone;
	String tradesmanAddress;
	String jobType;
	String jobDesc;
	Date jobDate;
	Double laborHours;
	Double laborCost;
	Double discount;
	Double totalPrice;
	String status;
	String invoiceNumber;
	Double afterVAT;
	Date invoiceDate = new Date();
	String transactionID;
	String paymentType;
    static constraints = {
		customerPhone nullable:true, blank:true;
		tradesmanAddress nullable:true, blank:true;
		tradesmanPhone nullable:true, blank:true;
		jobDesc nullable:true, blank:true;
		laborHours nullable:true, blank:true;
		laborCost nullable:true, blank:true;
		discount nullable:true, blank:true;
		status inList:["PAID","PENDING","SENT","OVERDUE"];
		jobDate nullable:true, blank:true;
		transactionID nullable:true, blank:true;
		paymentType nullable:true, blank:true, inList:["PAYPAL","AMAZON PAY"];
		customerAddress2 nullable:true, blank:true;
		customerAddress3 nullable:true, blank:true;
		customerAddress nullable:true, blank:true;
    }
}
