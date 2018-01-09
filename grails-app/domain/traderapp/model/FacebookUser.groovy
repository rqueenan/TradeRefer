package traderapp.model

import traderapp.user.User

class FacebookUser {
	static hasMany = [paymentMethod:String]
	String accessToken;
	String email;
	String name;
	String addressLine1;
	String addressLine2;
	String addressLine3;
	String postcode;
	String phone;
	User user;
	List paymentMethod;
	byte[] profilePic;
	String UID;
  
	static constraints = {
		email email: true;
		addressLine1 nullable:true, blank:true;
		addressLine2 nullable:true, blank:true;
		addressLine3 nullable:true, blank:true;
		postcode nullable:true, blank:true;
		phone nullable:true, blank:true;
		paymentMethod nullable:true, blank:true;
		profilePic nullable:true, blank:true;
	}
	
	static mapping = {
		profilePic sqlType: 'longblob'
	}
}
