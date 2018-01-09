package com.test

import grails.databinding.BindingFormat

class TestDomain {
	
	String testStr;
	
	@BindingFormat("dd MMM yyyy")
	Date testDate;
    static constraints = {
    }
}
