package business.externalinterfaces;
import java.util.*;

import business.exceptions.BusinessException;
import business.exceptions.RuleException;

public interface Rules  {
	String getModuleName();
	String getRulesFile();
	void prepareData();
	HashMap<String,DynamicBean> getTable();
	void runRules() throws BusinessException, RuleException;
	void populateEntities(List<String> updates);
	
	//updates are placed in a List -- object types may vary
	List<?> getUpdates();

}
