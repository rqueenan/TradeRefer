package traderapp.model

class Emails {
	String customerEmail;
	String tradesmanEmail;
	Date mailOneSentDate = new Date();
	Date mailTwoSentDate;
	Date followUpMailDate;
	boolean emailOneSent = true;
	boolean emailOneOpen = false;
	boolean emailTwoSent = false;
	boolean emailTwoOpen = false;
	boolean followUpMailSent = false;
	boolean followUpMailOpen = false;
	boolean registrationLinkClickedFirstMail = false;
	boolean registrationLinkClickedSecondMail = false;
	boolean registrationLinkClickedFollowUpMail = false;
	boolean customerRegistered = false;
	String invoiceNumber;
	boolean notifiedToTradesman = false;
    static constraints = {
		mailTwoSentDate nullable:true, blank:true;
		followUpMailDate nullable:true, blank:true;
    }
}
