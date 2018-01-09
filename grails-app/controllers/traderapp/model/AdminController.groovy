package traderapp.model

import java.lang.ProcessBuilder.Redirect

import groovy.util.logging.Slf4j

/**
 * For Admin View
 *
 */
@Slf4j
class AdminController {

    def index() { 
		
		
	}
	
	/**
	 * @return All trdaesman list in the system
	 */
	def tradesmanList(){
		def allTradesmen = Tradesman.findAll();
		[tradesmen:allTradesmen]
	}
	
	/**
	 * @return All customer list in the system
	 */
	def customerList(){
		def allCustomers = Customer.findAll();
		def allFbUsers = FacebookUser.findAll();
		allCustomers.addAll(allFbUsers);
		def allLiUsers = LinkedInUser.findAll();
		allCustomers.addAll(allLiUsers);
		[customers:allCustomers]
	}
	
	/**
	 * @return App level settings
	 */
	def settings(){
		AdminSettings settings = AdminSettings.get(1);
		[settings:settings]
	}
	
	/**
	 * Update admin settings
	 */
	def saveAdminSettings(Long id){
		AdminSettings settings = AdminSettings.get(id);
		settings.setVAT(params.vat?.toDouble());
		settings.save(flush:true);
		redirect action:'settings', model:[settings:settings]
	}
}
