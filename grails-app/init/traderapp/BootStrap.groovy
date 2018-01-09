package traderapp

import groovy.util.logging.Slf4j
import traderapp.model.AdminSettings
import traderapp.user.Role
import traderapp.user.User
import traderapp.user.UserRole

@Slf4j
class BootStrap {

    def init = { servletContext ->
		log.info "Welcome To TradeRefer !!"
		if(!Role.findByAuthority("ROLE_ADMIN")){
			log.info("Creating ROLE_ADMIN");
			def roleAdmin = new Role(authority:"ROLE_ADMIN").save(flush:true);
		}
		if(!Role.findByAuthority("ROLE_CUSTOMER")){
			log.info("Creating ROLE_CUSTOMER");
			def roleCustomer = new Role(authority:"ROLE_CUSTOMER").save(flush:true);
		}
		if(!Role.findByAuthority("ROLE_TRADESMAN")){
			log.info("Creating ROLE_TRADESMAN");
			def roleTradesman = new Role(authority:"ROLE_TRADESMAN").save(flush:true);
		}
		if(!Role.findByAuthority("ROLE_FACEBOOK_CUSTOMER")){
			log.info("Creating ROLE_FACEBOOK_CUSTOMER");
			def roleFacebookCustomer = new Role(authority:"ROLE_FACEBOOK_CUSTOMER").save(flush:true);
		}
		if(!Role.findByAuthority("ROLE_LINKEDIN_CUSTOMER")){
			log.info("Creating ROLE_LINKEDIN_CUSTOMER");
			def roleLinkedInCustomer = new Role(authority:"ROLE_LINKEDIN_CUSTOMER").save(flush:true);
		}
		def adminRole = Role.findByAuthority("ROLE_ADMIN");

		if(!UserRole.findByRole(adminRole)){
			log.info("Creating Admin User")
			def adminUser = new User(username:"trader_ref_admin", password:"trader@1234").save(flush:true);
			def userRole = new UserRole(user:adminUser, role:adminRole).save(flush:true);
		}
		if(AdminSettings.findAll().size() == 0){
			AdminSettings settings = new AdminSettings(VAT:20.0).save(flush:true);
		}
    }
    def destroy = {
    }
}
