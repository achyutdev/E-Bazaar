package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

/**
 * This class is concerned with managing the entire
 * list of catalogtypes -- this is different from
 * managing one particular catalog (which is managed
 * by DbClassCatalog)
 */
public class DbClassCatalogTypes implements DbClass {
	enum Type {GET_TYPES};
	@SuppressWarnings("unused")
	private static final Logger LOG = 
		Logger.getLogger(DbClassCatalogTypes.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
    
    private Type queryType;
    
    private String getTypesQuery = "SELECT * FROM CatalogType";
    private Object[] getTypesParams;
    private int[] getTypesTypes; //strange variable name, but follows naming convention
    
    //This is popuated by getTypesQuery 
    private CatalogTypesImpl types;
    
    public CatalogTypesImpl getCatalogTypes() throws DatabaseException {
        queryType = Type.GET_TYPES;
        getTypesParams = new Object[]{};  //empty
        getTypesTypes = new int[]{};  //empty
        dataAccessSS.atomicRead(this);	
        return types;        
    }
    
    @Override
    public String getQuery() {
    	switch(queryType) {
	    	case GET_TYPES :
	    		return getTypesQuery;
	    	default:
	    		return null;
    	}	
    }
    
    @Override
    public Object[] getQueryParams() {
    	switch(queryType) {
	    	case GET_TYPES :
	    		return getTypesParams;
	    	default:
	    		return null;
		}	
	}
    
    @Override
    public int[] getParamTypes() {
    	switch(queryType) {
	    	case GET_TYPES :
	    		return getTypesTypes;
	    	default:
	    		return null;
		}	
	}
   
    @Override
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        types = new CatalogTypesImpl();
        try {
            while(resultSet.next()){
                types.addCatalog(resultSet.getInt("catalogid"),
                        		resultSet.getString("catalogname"));
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }      
    }
    @Override
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    } 
}
