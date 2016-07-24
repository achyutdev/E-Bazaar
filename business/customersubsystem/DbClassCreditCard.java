package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import business.customersubsystem.DbClassAddress.Type;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassCreditCard implements DbClass {
	enum Type {READ_DEFAULTPAYMENT};
	private static final Logger LOG
	    = Logger.getLogger(DbClassCreditCard.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();

	DbClassCreditCard() {}
///// queries ///////
	private String readDefaultPaymentInfo
	     = "SELECT nameoncard,expdate,cardtype,cardnum " +
	     	"FROM customer where custid= ?";

	private Object[] readDefaultPaymentsParams;
	private int[] readDefaultPaymentsType;

	//this object is stored here, using setAddress, when it needs to be saved to db
    private Address address;

    //these are populated after database reads
    private CreditCardImpl defaultCreditCard;
    private Type queryType;

    CreditCard readDefaultPaymentInfo(CustomerProfile custProfile) throws DatabaseException {
    	queryType = Type.READ_DEFAULTPAYMENT;
    			readDefaultPaymentsParams = new Object[]
        	{custProfile.getCustId()};
    	readDefaultPaymentsType = new int[]
        	{Types.INTEGER};
        dataAccessSS.establishConnection(this);
        dataAccessSS.read();
        return defaultCreditCard;
    	//implement
    }
	@Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	}

	 @Override
	    public String getQuery() {
	        switch(queryType) {
		        case READ_DEFAULTPAYMENT :
		        	return readDefaultPaymentInfo;
		        default :
		        	return null;
	        }
	    }
	    @Override
	    public Object[] getQueryParams() {
	    	switch(queryType) {
	        case READ_DEFAULTPAYMENT :
	        	return readDefaultPaymentsParams;
	        default :
	        	return null;
	    	}
	    }

	    @Override
	    public int[] getParamTypes() {
	    	switch(queryType) {
	        case READ_DEFAULTPAYMENT :
	        	return readDefaultPaymentsType;
	        default :
	        	return null;
	    	}
	    }
	    ////// populate objects after reads ///////////

	    @Override
	    public void populateEntity(ResultSet rs) throws DatabaseException {
	    	System.out.println("populateEntity++......in dbclassCreditcard");
	    	switch(queryType) {
	        case READ_DEFAULTPAYMENT :
	        	populateDefaultPayment(rs);
	        default :
	        	//donothing
	    	}
	    }
	    private void populateDefaultPayment(ResultSet rs) throws DatabaseException{
	          if(rs != null){
	              try {
	                  while(rs.next()) {
	                	  String nameOnCard=rs.getString("nameoncard");
	                	    String expirationDate=rs.getString("expdate");;
	                	    String cardNum=rs.getString("cardnum");
	                	    String cardType=rs.getString("cardtype");
	                	    defaultCreditCard= new CreditCardImpl(nameOnCard,expirationDate,cardNum,cardType);
	                  }
	              }
	              catch(SQLException e){
	                  throw new DatabaseException(e);
	              }
	          }
	    }

}
