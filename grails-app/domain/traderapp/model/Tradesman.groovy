package traderapp.model

import traderapp.user.User

class Tradesman {
	static hasMany = [workType:String]
	List workType;
	String name;
	String addressLine1;
	String addressLine2;
	String addressLine3;
	String postcode;
	String phone;
	String companyName;
	String companyEmail;
	String companyNumber;
	String certificaionNumber;
	Double hourlyRate;
	String collection_sortCode;
	String collection_accountNo;
	String collection_accountName;
	User user;
	byte[] profilePic;
	String paypal_clientId;
	String paypal_clientSecret;
	String amazon_merchantId;
	String amazon_accessKey;
	String amazon_secretKey;
	String amazon_clientId;
	boolean paypalEnabled = false;
	boolean amazonPayEnabled = false;
    static constraints = {
		addressLine1 nullable:true, blank:true;
		addressLine2 nullable:true, blank:true;
		addressLine3 nullable:true, blank:true;
		postcode nullable:true, blank:true;
		phone nullable:true, blank:true;
		companyName nullable:true, blank:true;
		companyEmail email: true;
		companyNumber nullable:true, blank:true;
		certificaionNumber nullable:true, blank:true;
		hourlyRate nullable:true, blank:true;
		collection_sortCode nullable:true, blank:true;
		collection_accountNo nullable:true, blank:true;
		collection_accountName nullable:true, blank:true;
		profilePic nullable:true, blank:true;
		paypal_clientId nullable:true, blank:true;
		paypal_clientSecret nullable:true, blank:true;
		amazon_merchantId nullable:true, blank:true;
		amazon_accessKey nullable:true, blank:true;
		amazon_secretKey nullable:true, blank:true;
		amazon_clientId nullable:true, blank:true;
    }
	
	static mapping = {
		profilePic sqlType: 'longblob'
	}
}
