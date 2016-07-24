package business.customersubsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Address;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesSubsystem;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.rulesbeans.AddressBean;
import business.rulesubsystem.RulesSubsystemFacade;

class RulesAddress implements Rules {
	private HashMap<String, DynamicBean> table;
	private DynamicBean bean;
	private AddressImpl updatedAddress;
	private RulesConfigProperties config = new RulesConfigProperties();
	private boolean isShippingAddress;
	
	RulesAddress(Address address){
		bean = new AddressBean(address);
		isShippingAddress = address.isShippingAddress();
	}	
	
	
	///////////////implementation of interface
	public String getModuleName(){
		return config.getProperty(RulesConfigKey.ADDRESS_MODULE.getVal());
	}
	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.ADDRESS_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();	
		String deftemplate = config.getProperty(RulesConfigKey.ADDRESS_DEFTEMPLATE.getVal());
		table.put(deftemplate, bean);
		
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}
	
	public void runRules() throws BusinessException, RuleException {
		RulesSubsystem rules = new RulesSubsystemFacade();
		rules.runRules(this);		
	}
	/* Expect a list of address values, in order
	 * street, city, state ,zip
	 */
	public void populateEntities(List<String> updates){
		
		updatedAddress = new AddressImpl(updates.get(0),
				updates.get(1),
				updates.get(2),
				updates.get(3), 
				isShippingAddress,
				!isShippingAddress);
		
	}
	
	public List<AddressImpl> getUpdates() {
		List<AddressImpl> retVal = new ArrayList<AddressImpl>();
		retVal.add(updatedAddress);
		return retVal;
	}
	


}
