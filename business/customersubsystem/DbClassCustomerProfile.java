package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;


class DbClassCustomerProfile implements DbClass {
	enum Type {READ};
	@SuppressWarnings("unused")
	private static final Logger LOG = 
		Logger.getLogger(DbClassCustomerProfile.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
    
    private Type queryType;
    
    private String readQuery 
        = "SELECT custid,fname,lname FROM Customer WHERE custid = ?";
    private Object[] readParams;
    private int[] readTypes;
    
    /** Used for reading in values from the database */
    private CustomerProfileImpl customerProfile;
      
    public CustomerProfileImpl readCustomerProfile(Integer custId) throws DatabaseException {
        queryType = Type.READ;
        readParams = new Object[]{custId};
        readTypes = new int[]{Types.INTEGER};
        dataAccessSS.atomicRead(this); 
        return customerProfile;
    }
 
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        try {   
            //we take the first returned row
            if(resultSet.next()){
                customerProfile 
                  = new CustomerProfileImpl(resultSet.getInt("custid"),
                							resultSet.getString("fname"),
                                            resultSet.getString("lname"));
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
    }

    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    }

    @Override
    public String getQuery() {
    	switch(queryType) {
	    	case READ :
	    		return readQuery;
	    	default :
	    		return null;
    	}
    }

	@Override
	public Object[] getQueryParams() {
		switch(queryType) {
	    	case READ :
	    		return readParams;
	    	default :
	    		return null;
		}
	}
	@Override
	public int[] getParamTypes() {
		switch(queryType) {
	    	case READ :
	    		return readTypes;
	    	default :
	    		return null;
		}
	}
}
