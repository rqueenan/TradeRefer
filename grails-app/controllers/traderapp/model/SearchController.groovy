package traderapp.model

import grails.converters.JSON
import groovy.util.logging.Slf4j

/**
 * Contains methos that returns results
 * on auto complete of tradesman email address and company name,
 * Customer email address and Customer name
 */
@Slf4j
class SearchController {

    def index() { }
	
	/**
	 * Search for customer records from entered customer name
	 * @param term - Input from customer name text box 
	 * @return list of customer instances in which customer name is matched
	 */
	def searchByCustomerName(String term){
		List returnList = new ArrayList();
		log.info "searchByCustomerName() | term : $term";
		if(term.size() >= 2){
			def wildCardTerm = "%"+term+"%";
			
			List searchResult_customer = new ArrayList();
			List searchResult_facebookUser = new ArrayList();
			List searchResult_linkedInUser = new ArrayList();
			
			def c = Customer.createCriteria();
			searchResult_customer = c.list() {
				like('name',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("name")
					property("email")
					property("addressLine1")
					property("addressLine2")
					property("addressLine3")
					property("postcode")
				}
			}
			
			def fb = FacebookUser.createCriteria();
			searchResult_facebookUser = fb.list() {
				like('name',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("name")
					property("email")
					property("addressLine1")
					property("addressLine2")
					property("addressLine3")
					property("postcode")
				}
			}
			
			def li = LinkedInUser.createCriteria();
			searchResult_linkedInUser = li.list() {
				like('name',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("name")
					property("email")
					property("addressLine1")
					property("addressLine2")
					property("addressLine3")
					property("postcode")
				}
			}
			
			log.info "Customers found with term are $searchResult_customer";
			
			if(searchResult_customer?.size() > 0){
				searchResult_customer.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[1]);
					map.put("email", it[2]);
					map.put("addressLine1", it[3]);
					map.put("addressLine2", it[4]);
					map.put("addressLine3", it[5]);
					map.put("postcode", it[6]);
					returnList.add(map);
				}
			}
			
			if(searchResult_facebookUser?.size() > 0){
				searchResult_facebookUser.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[1]);
					map.put("email", it[2]);
					map.put("addressLine1", it[3]);
					map.put("addressLine2", it[4]);
					map.put("addressLine3", it[5]);
					map.put("postcode", it[6]);
					returnList.add(map);
				}
			}
			
			if(searchResult_linkedInUser?.size() > 0){
				searchResult_linkedInUser.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[1]);
					map.put("email", it[2]);
					map.put("addressLine1", it[3]);
					map.put("addressLine2", it[4]);
					map.put("addressLine3", it[5]);
					map.put("postcode", it[6]);
					returnList.add(map);
				}
			}
			
			log.info "searchResult : $returnList";
		}
		render returnList as JSON;
	}
	
	/**
	 * Search for tradesman records from entered company name
	 * @param term - Input from tradesman's company name text box 
	 * @return list of tradesman instances which matched entered value for company name
	 */
	def searchByTradesmanCompanyName(String term){
		List returnList = new ArrayList();
		log.info "searchByTradesmanCompanyName() | term : $term";
		if(term.size() >= 2){
			def wildCardTerm = "%"+term+"%";
			
			List searchResult_tradesman=new ArrayList();
			
			def c = Tradesman.createCriteria();
			searchResult_tradesman = c.list() {
				like('companyName',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("companyName")
					property("companyEmail")
				}
			}
			
			log.info "Tradesman found with term are $searchResult_tradesman";
			
			if(searchResult_tradesman?.size() > 0){
				searchResult_tradesman.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[1]);
					map.put("email", it[2]);
					returnList.add(map);
				}
			}
			
			log.info "searchResult : $returnList";
		}
		render returnList as JSON;
	}
	
	/**
	 * Search for tradesman records from entered tradesman email address
	 * @param term - Input from tradesman's company email address text box 
	 * @return list of tradesman instances which matched entered value for tradesman email address
	 */
	def searchByTradesmanEmail(String term){
		List returnList = new ArrayList();
		log.info "searchByTradesmanEmail() | term : $term";
		if(term.size() >= 2){
			def wildCardTerm = "%"+term+"%";
			
			List searchResult_tradesman=new ArrayList();
			
			def c = Tradesman.createCriteria();
			searchResult_tradesman = c.list() {
				like('companyEmail',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("companyName")
					property("companyEmail")
				}
			}
			
			log.info "Tradesman found with term are $searchResult_tradesman";
			
			if(searchResult_tradesman?.size() > 0){
				searchResult_tradesman.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[2]);
					map.put("email", it[2]);
					returnList.add(map);
				}
			}
			
			log.info "searchResult : $returnList";
		}
		render returnList as JSON;
	}
	
	/**
	 * Search for customer records from entered customer email address
	 * @param term - Input from customer email address text box 
	 * @return list of customer/facebook user/linkedin user instances in which 
	 * customer email address is matched
	 */
	def searchByCustomerEmail(String term){
		List returnList = new ArrayList();
		log.info "searchByCustomerEmail() | term : $term";
		if(term.size() >= 2){
			def wildCardTerm = "%"+term+"%";
			
			
			//Search in Customers
			List searchResult_customer=new ArrayList();
			
			def c = Customer.createCriteria();
			searchResult_customer = c.list() {
				like('email',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("name")
					property("email")
					property("addressLine1")
					property("addressLine2")
					property("addressLine3")
					property("postcode")
				}
			}
			
			log.info "Customers found with term are $searchResult_customer";
			
			//Search in FB users
			List searchResult_fb=new ArrayList();
			
			def fb = FacebookUser.createCriteria();
			searchResult_fb = fb.list() {
				like('email',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("name")
					property("email")
					property("addressLine1")
					property("addressLine2")
					property("addressLine3")
					property("postcode")
				}
			}
			
			log.info "FB users found with term are $searchResult_fb";
			
			//Search in LinkedIn Users
			List searchResult_li = new ArrayList();
			
			def li = LinkedInUser.createCriteria();
			searchResult_li = li.list(){
				like('email',wildCardTerm);
				projections { // good to select only the required columns.
					property("id")
					property("name")
					property("email")
					property("addressLine1")
					property("addressLine2")
					property("addressLine3")
					property("postcode")
				}
			}
			
			log.info "LinkedIn users found with term are $searchResult_li";
			
			if(searchResult_customer?.size() > 0){
				searchResult_customer.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[2]);
					map.put("email", it[2]);
					map.put("addressLine1", it[3]);
					map.put("addressLine2", it[4]);
					map.put("addressLine3", it[5]);
					map.put("postcode", it[6]);
					returnList.add(map);
				}
			}
			
			if(searchResult_fb?.size() > 0){
				searchResult_fb.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[2]);
					map.put("email", it[2]);
					map.put("addressLine1", it[3]);
					map.put("addressLine2", it[4]);
					map.put("addressLine3", it[5]);
					map.put("postcode", it[6]);
					returnList.add(map);
				}
			}
			
			if(searchResult_li?.size() > 0){
				searchResult_li.each{
					Map map = new HashMap();
					map.put("id", it[0]);
					map.put("key", it[0]);
					map.put("name", it[1]);
					map.put("label", it[2]);
					map.put("email", it[2]);
					map.put("addressLine1", it[3]);
					map.put("addressLine2", it[4]);
					map.put("addressLine3", it[5]);
					map.put("postcode", it[6]);
					returnList.add(map);
				}
			}
			
			log.info "searchResult : $returnList";
		}
		render returnList as JSON;
	}
	
	/**
	 * Get list of work types selected by tradesman
	 * @param id - Tradesman Instance Id
	 * @return list of work types selected by tradesman
	 */
	def getWorkTypes(Long id){
		log.info "getWorkTypes()"
		if(id){
			Tradesman tradesman = Tradesman.get(id);
			render tradesman.workType as JSON;
		}
	}
}
