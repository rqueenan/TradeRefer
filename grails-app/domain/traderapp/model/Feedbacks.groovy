package traderapp.model

class Feedbacks {
	Double starCounts;
	String comment;
    static constraints = {
		comment nullable:true, blank:true;
    }
}
