
package business.usecasecontrol;


import business.BusinessConstants;
import business.DbClassLogin;
import business.Login;
import business.SessionCache;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.exceptions.UserException;
import business.externalinterfaces.CustomerSubsystem;


public class LoginControl {
	
	//returns authorization level if authenticated
	public int authenticate(Login login) throws UserException, BackendException {
		
		DbClassLogin dbClass = new DbClassLogin(login);
        if(!dbClass.authenticate()) {
        	throw new UserException("Authentication failed for ID: " + login.getCustId());
        }
        return dbClass.getAuthorizationLevel();
        
	}
	
	public CustomerSubsystem prepareAndStoreCustomerObject(Login login, int authorizationLevel) throws BackendException {
		CustomerSubsystem customer = new CustomerSubsystemFacade();
		//need to place into SessionContext immediately since the facade will be used during
		//initialization; alternative: createAddress, createCreditCard methods
		//made to be static
		SessionCache cache = SessionCache.getInstance();
		cache.add(BusinessConstants.LOGGED_IN, Boolean.TRUE);
		cache.add(BusinessConstants.CUSTOMER, customer);
        
        //finish initialization
        customer.initializeCustomer(login.getCustId(), authorizationLevel);
        return customer;
	}
    
}
