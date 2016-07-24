package business.externalinterfaces;

import business.exceptions.BusinessException;
import business.exceptions.RuleException;

public interface RulesSubsystem {
	public void runRules(Rules rulesIface) throws BusinessException,RuleException;
	
}
