package traderapp

import groovy.util.logging.Slf4j
import traderapp.model.Invoice
import traderapp.model.Jobs
import traderapp.user.Role
import traderapp.user.UserRole

/**
 * Redirects back to corresponding pages for loggedin Users
 */
@Slf4j
class HomeController {

	def springSecurityService
	def invoiceEmailsService
	
	
    /**
     * Default URL after login. It redirects back to corresponding 
     * default URL of loggedIn user
     */
    def index() { 
		def user = springSecurityService.currentUser
		log.info "index() | Redirecting to default url after login"
		if(user){
			log.info "user id : $user.id";
			UserRole loggedInUserRole = UserRole.findByUser(user);
			Role loggedInRole = loggedInUserRole?.role;
			log.info "Logged in user role is : $loggedInRole.authority";
			if (loggedInRole.authority == 'ROLE_ADMIN') {
				redirect controller: 'admin', action: 'settings'
			} else if (loggedInRole.authority == 'ROLE_TRADESMAN') {
				redirect controller: 'tradesman', action: 'showJobs'
			} else if (loggedInRole.authority == 'ROLE_CUSTOMER' || loggedInRole.authority == 'ROLE_FACEBOOK_CUSTOMER' || loggedInRole.authority == 'ROLE_LINKEDIN_CUSTOMER') {
				redirect controller: 'profile', action: 'jobs';
			} else {
				redirect view:'index';
			}
		} else{
			log.info "User session time out or user is not logged in. Redirecting to Login Page"
			redirect controller:'login', action:'auth'
		}
	}
	
	def error(){
		println "url failed"
		redirect controller:'login', action:'auth'
	}
	
	
	
	
	
	
}
