package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.logging.Logger;

import business.externalinterfaces.Catalog;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

/**
 * This class is concerned with managing data for a single
 * catalog. To read or update the entire list of catalogs in
 * the database, see DbClassCatalogs
 *
 */
public class DbClassCatalog implements DbClass {
	enum Type {INSERT,DELETE,UPDATE};
	@SuppressWarnings("unused")
	private static final Logger LOG = 
		Logger.getLogger(DbClassCatalog.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
	
	private Type queryType;
	
	private String insertQuery = "INSERT into CatalogType (catalogname) VALUES(?)"; 
	private String deleteQuery= "DELETE FROM CatalogType Where catalogid=?";
	private String updateQuery= "UPDATE CatalogType  SET catalogname =? Where catalogid=?";
	private Object[] insertParams,deleteParams,updateParams;
	private int[] insertTypes,deleteTypes,updateTypes;
    
    public int saveNewCatalog(String catalogName) throws DatabaseException {
    	queryType = Type.INSERT;
    	insertParams = new Object[]{catalogName};
    	insertTypes = new int[]{Types.VARCHAR};
    	return dataAccessSS.insertWithinTransaction(this);  	
    }
    
    @Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}
    
    @Override
	public String getQuery() {
		switch(queryType) {
			case INSERT:
				return insertQuery;
			case DELETE:
				return deleteQuery;
			case UPDATE:
				return updateQuery;
			default:
				return null;
		}
	}
    @Override
   	public Object[] getQueryParams() {
   		switch(queryType) {
   			case INSERT:
   				return insertParams;
   			case DELETE:
				return deleteParams;
   			case UPDATE:
   				return updateParams;
   			default:
   				return null;
   		}
    }		
	 @Override
	public int[] getParamTypes() {
		 switch(queryType) {
			case INSERT:
				return insertTypes;
			case DELETE:
				return deleteTypes;
			case UPDATE:
				return updateTypes;
			default:
				return null;
		}
	 }
    @Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		// do nothing
		
	}
    
    public void deleteCatalog(int catalogId) throws DatabaseException {
    	queryType=Type.DELETE;
    	deleteParams=new Object[]{catalogId};
    	deleteTypes=new int[]{Types.INTEGER};
    	dataAccessSS.deleteWithinTransaction(this);
    }
    
    public void updateCatalog(Catalog catalog) throws DatabaseException{
    	queryType=Type.UPDATE;
    	updateParams=new Object[]{catalog.getName(),catalog.getId()};
    	updateTypes=new int[]{Types.VARCHAR,Types.INTEGER};
    	dataAccessSS.updateWithinTransaction(this);
    	
    }
	
}
