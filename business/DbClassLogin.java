package business;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import business.exceptions.BackendException;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassLogin implements DbClass {
	enum Type {AUTH};
	private static final Logger LOG = 
		Logger.getLogger(DbClassLogin.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
	private Type queryType;
	
	private String authQuery = "SELECT * FROM Customer WHERE custid = ? AND password = ?";
	private Object[] authParams;
	private int[] authTypes;
	
    private Integer custId;
    int authorizationLevel;
    private String password;
    private boolean authenticated = false;
    
    public DbClassLogin(Login login){
        this.custId = login.getCustId();
        this.password = login.getPassword();
    }
    public boolean authenticate() throws BackendException {
    	LOG.info("Authenticating");
    	queryType = Type.AUTH;
    	authParams = new Object[] {custId, password};
    	authTypes = new int[]{Types.INTEGER, Types.VARCHAR};
    	try {
    		dataAccessSS.atomicRead(this);
    	} catch(DatabaseException e ) {
    		throw new BackendException(e);
    	}
        return authenticated;        
    }
    
    public int getAuthorizationLevel() {
    	LOG.info("authorizationLevel = " + authorizationLevel);
        return authorizationLevel;
    }
    
    @Override
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
    	switch(queryType) {
	    	case AUTH :
		        try {
		            if(resultSet.next()) {
		            	authorizationLevel = resultSet.getInt("admin");
		            	authenticated = true;
		            } //else authenticated = false;
		        }
		        catch(SQLException e){
		            throw new DatabaseException(e);
		        }
		        break;
	        default:
	        	//do nothing
	    }
    }
    @Override
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    }

    @Override
    public String getQuery() {
    	switch(queryType) {
    		case AUTH :
    			return authQuery;
    		default:
    			return null;
    	}
		
    }
    @Override
    public Object[] getQueryParams() {
    	switch(queryType) {
			case AUTH :
				return authParams;
			default:
				return null;
    	}
    }
    
    @Override
    public int[] getParamTypes() {
    	switch(queryType) {
			case AUTH :
				return authTypes;
			default:
				return null;
		}
    }
}
