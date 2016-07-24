package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassAddressForTest;

class DbClassAddress implements DbClass, DbClassAddressForTest {
	enum Type {INSERT, READ_ALL, READ_DEFAULT_BILL, READ_DEFAULT_SHIP};
	private static final Logger LOG
	    = Logger.getLogger(DbClassAddress.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();

	DbClassAddress() {}

    //used when an Address object needs to be saved to the db
	void setAddress(Address addr) {
		address = addr;
	}
	///// queries ///////
	private String insertQuery = "INSERT into altaddress " +
        		"(custid,street,city,state,zip,isship,isbill) " +
        		"VALUES(?,?,?,?,?,?,?)";
	private String readAllQuery
	     = "SELECT * from altaddress WHERE custid = ?";
	      //param value to set: custProfile.getCustId()
	private String readDefaultBillQuery = "SELECT billaddress1, billaddress2, billcity, billstate, billzipcode " +
                "FROM Customer WHERE custid = ?";
	            //param value to set: custProfile.getCustId()
	private String readDefaultShipQuery = "SELECT shipaddress1, shipaddress2, shipcity, shipstate, shipzipcode "+
        "FROM Customer WHERE custid = ?" ;
        //param value to set: custProfile.getCustId()

	private Object[] insertParams, readAllParams, readDefaultBillParams, readDefaultShipParams;
	private int[] insertTypes, readAllTypes, readDefaultBillTypes, readDefaultShipTypes;

	//this object is stored here, using setAddress, when it needs to be saved to db
    private Address address;

    //these are populated after database reads
    private List<Address> addressList;
    private AddressImpl defaultShipAddress;
    private AddressImpl defaultBillAddress;
    private Type queryType;


	//column names for Address table
    private final String STREET="street";
    private final String CITY = "city";
    private final String STATE = "state";
    private final String ZIP = "zip";
    private final String ISSHIP = "isship";
    private final String ISBILL = "isbill";

    //Precondition: Address has been set in this object
    void saveAddress(CustomerProfile custProfile) throws DatabaseException {
        queryType = Type.INSERT;
        insertParams = new Object[]
        	{custProfile.getCustId(),address.getStreet(), address.getCity(), address.getState(),address.getZip()
        			,address.isShippingAddress()?1:0,address.isBillingAddress()?1:0};
        insertTypes = new int[]
        	{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.INTEGER,Types.INTEGER};
        dataAccessSS.insertWithinTransaction(this);
    }

    Address readDefaultShipAddress(CustomerProfile custProfile) throws DatabaseException {
    	queryType = Type.READ_DEFAULT_SHIP;
    	readDefaultShipParams = new Object[]
        	{custProfile.getCustId()};
        readDefaultShipTypes = new int[]
        	{Types.INTEGER};
        dataAccessSS.establishConnection(this);
        dataAccessSS.read();
        return defaultShipAddress;
    	//implement
    }

    void deleteAddress(CustomerProfile custProfile) throws DatabaseException {

    	//implement
    }
    Address readDefaultBillAddress(CustomerProfile custProfile) throws DatabaseException {
    	queryType = Type.READ_DEFAULT_BILL;
    	readDefaultBillParams = new Object[]
        	{custProfile.getCustId()};
    	readDefaultBillTypes = new int[]
        	{Types.INTEGER};
        dataAccessSS.establishConnection(this);
        dataAccessSS.read();
        return defaultBillAddress;
    	//implement
    }
    public List<Address> readAllAddresses(CustomerProfile custProfile) throws DatabaseException {
    	//LOG.warning("Method readAllAddresses(CustomerProfile custProfile) has not been implemented");
    	queryType = Type.READ_ALL;
    	readAllParams = new Object[]
        	{custProfile.getCustId()};
    	readAllTypes = new int[]
        	{Types.INTEGER};
       System.out.println("dbclassAddress:readalladdress.............");
       dataAccessSS.establishConnection(this);
    	dataAccessSS.read();
    	 System.out.println("addresslist:"+addressList.size());
    	return addressList;
    }

    @Override
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());

    }
    @Override
    public String getQuery() {
        switch(queryType) {
	        case INSERT :
	        	return insertQuery;
	        case READ_ALL:
	        	return readAllQuery;
	        case READ_DEFAULT_BILL:
	        	return readDefaultBillQuery;
	        case READ_DEFAULT_SHIP:
	        	return readDefaultShipQuery;
	        default :
	        	return null;
        }
    }
    @Override
    public Object[] getQueryParams() {
    	switch(queryType) {
	        case INSERT :
	        	return insertParams;
	        case READ_ALL:
	        	return readAllParams;
	        case READ_DEFAULT_BILL:
	        	return readDefaultBillParams;
	        case READ_DEFAULT_SHIP:
	        	return readDefaultShipParams;
	        default :
	        	return null;
	    }
    }

    @Override
    public int[] getParamTypes() {
    	switch(queryType) {
	        case INSERT :
	        	return insertTypes;
	        case READ_ALL:
	        	return readAllTypes;
	        case READ_DEFAULT_BILL:
	        	return readDefaultBillTypes;
	        case READ_DEFAULT_SHIP:
	        	return readDefaultShipTypes;
	        default :
	        	return null;
	    }
    }
    ////// populate objects after reads ///////////

    @Override
    public void populateEntity(ResultSet rs) throws DatabaseException {
    	System.out.println("populateEntity++......");
    	switch(queryType) {
    	case READ_ALL:
    		System.out.println("pupulateAddresslist");
    		populateAddressList(rs);
    		break;
    	case READ_DEFAULT_SHIP:
    		System.out.println("populateDefaultShipAddress");
    		populateDefaultShipAddress(rs);
    		break;
    	case READ_DEFAULT_BILL:
    		System.out.println("populateDefaultBillAddress");
    		populateDefaultBillAddress(rs);
    		break;
    	default:
    		//do nothing
    	}
    }
    void populateAddressList(ResultSet rs) throws DatabaseException {
        addressList = new LinkedList<Address>();
        if(rs != null){
            try {
                while(rs.next()) {
                    address = new AddressImpl();
                    String str = rs.getString(STREET);
                    address.setStreet(str);
                    address.setCity(rs.getString(CITY));
                    address.setState(rs.getString(STATE));
                    address.setZip(rs.getString(ZIP));
                    address.setShippingAddress(rs.getBoolean(ISSHIP));
                    address.setBillingAddress(rs.getBoolean(ISBILL));
                    addressList.add(address);
                }
            }
            catch(SQLException e){
                throw new DatabaseException(e);
            }
        }
    }

    void populateDefaultShipAddress(ResultSet rs) throws DatabaseException {
    	defaultShipAddress = new AddressImpl();
          if(rs != null){
              try {
                  while(rs.next()) {
                      String str = rs.getString("shipaddress1");
                      defaultShipAddress.setStreet(str);
                      defaultShipAddress.setCity(rs.getString("shipcity"));
                      defaultShipAddress.setState(rs.getString("shipstate"));
                      defaultShipAddress.setZip(rs.getString("shipzipcode"));
                  }
              }
              catch(SQLException e){
                  throw new DatabaseException(e);
              }
          }

    }
    void populateDefaultBillAddress(ResultSet rs) throws DatabaseException {
    	defaultBillAddress = new AddressImpl();
        if(rs != null){
            try {
                while(rs.next()) {
                    String str = rs.getString("billaddress1");
                    defaultBillAddress.setStreet(str);
                    defaultBillAddress.setCity(rs.getString("billcity"));
                    defaultBillAddress.setState(rs.getString("billstate"));
                    defaultBillAddress.setZip(rs.getString("billzipcode"));
                }
            }
            catch(SQLException e){
                throw new DatabaseException(e);
            }
        }
    }



    public static void main(String[] args){
        DbClassAddress dba = new DbClassAddress();
        CustomerProfile cp = new CustomerProfileImpl(1, "John", "Smith");
        try {
        	System.out.println(dba.readAllAddresses(cp));
        }
        catch(DatabaseException e){
            e.printStackTrace();
        }
    }

}
