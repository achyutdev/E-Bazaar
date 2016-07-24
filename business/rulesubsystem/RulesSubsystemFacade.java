package business.rulesubsystem;

import java.io.*;
import java.net.URL;
import java.util.List;

import rulesengine.OperatingException;
import rulesengine.ReteWrapper;
import rulesengine.ValidationException;
//import system.rulescore.rulesengine.*;
//import system.rulescore.rulesupport.*;
//import system.rulescore.util.*;
import business.exceptions.BusinessException;
import business.exceptions.ParseException;
import business.exceptions.RuleException;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.externalinterfaces.RulesSubsystem;

public class RulesSubsystemFacade implements RulesSubsystem {
	 private final String dirPrefix =
	            (new RulesConfigProperties()).getProperty(RulesConfigKey.DIR_PREFIX.getVal());

	public void runRules(Rules rulesIface) throws BusinessException,RuleException {
		rulesIface.prepareData();
		ReteWrapper wrapper = new ReteWrapper();
		String nameOfRulesFile = rulesIface.getRulesFile();
		File rulesFile =new File(nameOfRulesFile);
		
		//String rulesAsString = readFile(nameOfRulesFile);
		
		wrapper.setTable(rulesIface.getTable());
		wrapper.setCurrentModule(rulesIface.getModuleName());
		try {
			URL url = this.getClass().getClassLoader().
                    getResource(dirPrefix + nameOfRulesFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            wrapper.setRulesAsString(reader);
			wrapper.runRules();
			List<String> updates = wrapper.getUpdates();
			rulesIface.populateEntities(updates);
		}
		catch(IOException iox) {
			throw new BusinessException(iox.getMessage());
		}
		catch(ValidationException vx){
			throw new RuleException(vx.getMessage());
		}
		catch(OperatingException ox){
			throw new BusinessException(ox.getMessage());
		}
		
	}
	/* this is used if the rules are accessible from this project are not
	 * not encrypted -- not used in EBazaar
	 */
	String readFile(String filename) throws ParseException {
		String theString = null;
		String newline = System.getProperty("line.separator");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while( (line = reader.readLine()) != null){
				sb.append(line + newline);
			}
			theString = sb.toString();
			
		}
		catch(IOException e) {
			
			throw new ParseException(e.getMessage());
			
		}
		return theString;		
	}
	
}
