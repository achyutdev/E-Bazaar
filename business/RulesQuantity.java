package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesSubsystem;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.rulesbeans.QuantityBean;
import business.rulesubsystem.RulesSubsystemFacade;

public class RulesQuantity implements Rules {

	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;
	private RulesConfigProperties config = new RulesConfigProperties();
	
	public RulesQuantity(int quantityAvail, String quantityRequested){
		bean = new QuantityBean(quantityRequested, quantityAvail);
	}
	public String getModuleName() {
		return config.getProperty(RulesConfigKey.QUANTITY_MODULE.getVal());
	}

	public String getRulesFile() {
		return config.getProperty(RulesConfigKey.QUANTITY_RULES_FILE.getVal());
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		String deftemplate = config.getProperty(RulesConfigKey.QUANTITY_DEFTEMPLATE.getVal());
		table.put(deftemplate, bean);
	}
	public void runRules() throws BusinessException, RuleException {
    	RulesSubsystem rules = new RulesSubsystemFacade();
    	rules.runRules(this);
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}

	public List<Object> getUpdates() {
		// nothing to do
		return new ArrayList<Object>();
	}

	public void populateEntities(List<String> updates) {
		// nothing to do
		
	}




}
