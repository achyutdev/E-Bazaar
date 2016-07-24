
package business.shoppingcartsubsystem;

import static business.util.StringParse.makeString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.ShoppingCart;


public class DbClassShoppingCart implements DbClass {
	enum Type {GET_ID, GET_SAVED_ITEMS, GET_TOP_LEVEL_SAVED_CART,
		SAVE_CART, SAVE_CART_ITEM, DELETE_CART, DELETE_ALL_CART_ITEMS};
	
	private static final Logger LOG = Logger.getLogger(DbClassShoppingCart.class
			.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	Type queryType;
	
	///////queries and params
	private String getIdQuery = "SELECT shopcartid FROM ShopCartTbl WHERE custid = ?";
	   //param is custProfile.getCustId()
	private String saveCartQuery = "INSERT INTO shopcarttbl (custid,shipaddress1, " + 
    		"shipaddress2, shipcity, shipstate, shipzipcode, billaddress1, " + 
    		"billaddress2, billcity, billstate, billzipcode, nameoncard, " +
    		"expdate,cardtype, cardnum, totalpriceamount, totalshipmentcost, "+ 
    		"totaltaxamount, totalamountcharged) " +
    		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private String getTopLevelSavedCartQuery = "SELECT * FROM shopcarttbl WHERE shopcartid = ?";
			//param is cartId 
	private String getSavedItemsQuery  = "SELECT * FROM shopcartitem WHERE shopcartid = ?";
			//param is cartId 
	private String saveCartItemQuery = "INSERT INTO shopcartitem (shopcartid, productid, quantity, totalprice, shipmentcost, taxamount) " +
    		"VALUES (?,?,?,?,?,?)";
	private String deleteCartQuery = "DELETE FROM shopcarttbl WHERE shopcartid = ?";
			//param is cartId.intValue()
	private String deleteAllCartItemsQuery = "DELETE FROM shopcartitem WHERE shopcartid = ?";
		    //param is cartId.intValue()
	
	private Object[] getIdParams, saveCartParams, getTopLevelSavedCartParams, getSavedItemsParams,
	                 saveCartItemParams, deleteCartParams, deleteAllCartItemsParams;
	private int[] getIdTypes, saveCartTypes, getTopLevelSavedCartTypes, getSavedItemsTypes,
	                 saveCartItemTypes, deleteCartTypes, deleteAllCartItemsTypes;
	
    ShoppingCartImpl cartImpl;
    ShoppingCart cart;//this is inserted to support a save operation
    CartItem cartItem;//this is inserted to support a save operation
    List<CartItem> cartItemsList;
    Integer cartId;  //used once when read, but don't use other times
    
    DbClassShoppingCart() {}
    
    public void saveCart(CustomerProfile custProfile, ShoppingCart cart) 
    		throws DatabaseException {
    	//This will be used by support methods
    	this.cart = cart;
    	Integer cartId = null;
    	
    	//this.custProfile = custProfile;
	    List<CartItem> cartItems = cart.getCartItems();

	    //Begin transaction
	    dataAccessSS.establishConnection(this);
	    dataAccessSS.startTransaction();
	    try { 
	    	
	    	//If customer has a saved cart already, get its cartId -- will delete
		    //this cart as part of the transaction
		    Integer oldCartId = getShoppingCartId(custProfile);
	    	
	    	//First, delete old cart in two steps
	    	if(oldCartId != null) {
	    		deleteCart(oldCartId);
	    		deleteAllCartItems(oldCartId);
	    	}
	    	    	
		    //Second, save top level of cart to be saved
		    cartId = saveCartTopLevel(custProfile);  //returns new cartId
	    
		    //Finally, save the associated cartitems in a loop
		    //We have the cartId for these cartitems
		    for(CartItem item : cartItems){
		    	item.setCartId(cartId);	        
		        saveCartItem(item);
		    }
		    dataAccessSS.commit();
		    
	    } catch(DatabaseException e) {
        	dataAccessSS.rollback();
        	LOG.warning("Rolling back...");
        	throw (e);
        }  finally {
        	dataAccessSS.releaseConnection();
        }
    }
   
    //Support method for saveCart -- part of another transaction started within saveCart
    private void deleteCart(Integer cartId) throws DatabaseException {
    	queryType = Type.DELETE_CART;
    	deleteCartParams = new Object[]{cartId};
    	deleteCartTypes = new int[]{Types.INTEGER};
    	dataAccessSS.delete();  	
    }
    
    //Support method for saveCart -- part of another transaction started within saveCart
    private void deleteAllCartItems(Integer cartId) throws DatabaseException {
    	queryType = Type.DELETE_ALL_CART_ITEMS;
    	deleteAllCartItemsParams = new Object[]{cartId};
    	deleteAllCartItemsTypes = new int[]{Types.INTEGER};
    	dataAccessSS.delete();
    }
    
     //support method for saveCart and also for retrieveSavedCart; part of another transaction
    private Integer getShoppingCartId(CustomerProfile custProfile) throws DatabaseException {
        queryType = Type.GET_ID;
        getIdParams = new Object[]{custProfile.getCustId()};
        getIdTypes = new int[]{Types.INTEGER};
        dataAccessSS.read(); //value from read is stored in cartId instance variable 
        return cartId;
    }
    
    //Support method for saveCart -- part of another transaction started within saveCart
    //Precondition: shopping cart was stored as instance variable (should be done by saveCart method)
    private int saveCartTopLevel(CustomerProfile custProfile) throws DatabaseException {
    	queryType = Type.SAVE_CART;
    	saveCartParams = new Object[]{custProfile.getCustId(), cart.getShippingAddress().getStreet(),
    	  cart.getShippingAddress().getCity(), cart.getShippingAddress().getState(), cart.getShippingAddress().getZip(),
    	  cart.getBillingAddress().getStreet(), cart.getBillingAddress().getCity(), cart.getBillingAddress().getState(),
    	  cart.getBillingAddress().getZip(), cart.getPaymentInfo().getNameOnCard(), cart.getPaymentInfo().getExpirationDate(),
    	  cart.getPaymentInfo().getCardType(), cart.getPaymentInfo().getCardNum(), cart.getTotalPrice(),
    	  0.00, 0.00, cart.getTotalPrice(),0.0,0.0};
    	saveCartTypes = new int[]{Types.INTEGER, Types.VARCHAR, 
    			Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
    			Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
    			Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
    			Types.VARCHAR,Types.VARCHAR,Types.DOUBLE,
    			Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE};	
    	return dataAccessSS.insert();
    }
    
    //Support method for saveCart -- part of another transaction started within saveCart
    private void saveCartItem(CartItem item) throws DatabaseException {
    	this.cartItem = item;
    	queryType = Type.SAVE_CART_ITEM;
    	saveCartItemParams = new Object[]{cartItem.getCartid(), cartItem.getProductid(), Integer.parseInt(cartItem.getQuantity()),
    			Double.parseDouble(cartItem.getTotalprice()), 0.00, 0.00};		
    	saveCartItemTypes = new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER,
    			Types.DOUBLE, Types.DOUBLE, Types.DOUBLE};
    	dataAccessSS.insert();
    }
    
   
    ShoppingCartImpl retrieveSavedCart(CustomerProfile custProfile) throws DatabaseException {
    	dataAccessSS.establishConnection(this);
	    dataAccessSS.startTransaction();
	    try { 
	    	ShoppingCartImpl cart = null;
	    	//First, get cartId
			Integer cartId = getShoppingCartId(custProfile);
	    	
			//Second, if saved cart found, get top level cart data
			if(cartId != null) {
				cart = getTopLevelSavedCart(cartId);
				
				//Last, get cart items associated with this cart id, and insert into cart
			    List<CartItem> items = getSavedCartItems(cartId);
			    cart.setCartItems(items);
			}
		     
		    dataAccessSS.commit();
		    return cart;
		    
	    } catch(DatabaseException e) {
        	dataAccessSS.rollback();
        	LOG.warning("Rolling back...");
        	throw (e);
        }  finally {
        	//dataAccessSS.releaseConnection(this);
        	dataAccessSS.releaseConnection();
        }	
    }  
    
    //support method for retrieveSavedCart -- this is part of transaction that begins in
    //retrieveSavedCart
    private List<CartItem> getSavedCartItems(Integer cartId) throws DatabaseException {
        queryType = Type.GET_SAVED_ITEMS;
        getSavedItemsParams = new Object[]{cartId};
        getSavedItemsTypes = new int[]{Types.INTEGER};
        dataAccessSS.read();
        return cartItemsList;      
    }
    
    //support method for retrieveSavedCart -- this is part of transaction that begins in
    //retrieveSavedCart
    private ShoppingCartImpl getTopLevelSavedCart(Integer cartId) throws DatabaseException {
        queryType = Type.GET_TOP_LEVEL_SAVED_CART;
        getTopLevelSavedCartParams = new Object[]{cartId};
        getTopLevelSavedCartTypes = new int[]{Types.INTEGER};
        dataAccessSS.read(); //stores value in cartImpl
        return cartImpl;
    }
     
    

    public void populateEntity(ResultSet resultSet) throws DatabaseException {
    	switch(queryType) {
	    	case GET_ID:
	            populateShopCartId(resultSet);
	            break;
	    	case GET_SAVED_ITEMS:
	        	try {
	        		populateCartItemsList(resultSet);
	        	} catch(BackendException e) {
	        		throw new DatabaseException(e);
	        	}
	        	break;
	    	case GET_TOP_LEVEL_SAVED_CART:
	        	populateTopLevelCart(resultSet);
	        	break;
	        default:
	        	//do nothing
        }  
    }
    private void populateShopCartId(ResultSet rs){
        try {
            if(rs.next()){
                cartId = rs.getInt("shopcartid");
            }
        }
        catch(SQLException e) {
            //do nothing
        }   
    }
    
    private void populateTopLevelCart(ResultSet rs) throws DatabaseException {
    	cartImpl = new ShoppingCartImpl();
    	Address shippingAddress = null;
    	Address billingAddress = null;
    	CreditCard creditCard = null;
    	try {
    		if(rs.next()) {
    			//load shipping address
    			String shipStreet = rs.getString("shipaddress1");
    			String shipCity = rs.getString("shipcity");
    			String shipState = rs.getString("shipstate");
    			String shipZip = rs.getString("shipzipcode");
    			shippingAddress = 
    				CustomerSubsystemFacade.createAddress(shipStreet, shipCity, shipState, shipZip, true, false);
    			
    			//load billing address
    			String billStreet = rs.getString("shipaddress1");
    			String billCity = rs.getString("shipcity");
    			String billState = rs.getString("shipstate");
    			String billpZip = rs.getString("shipzipcode");
    			billingAddress = 
    				CustomerSubsystemFacade.createAddress(billStreet, billCity, billState, billpZip,false, true);
    			
    			//load credit card: createCreditCard(String name, String num, String type, expDate)
    			String name = rs.getString("nameoncard");
    			String num  = rs.getString("cardnum");
    			String type = rs.getString("cardtype");
    			String exp  = rs.getString("expdate");
    			creditCard 
    			  = CustomerSubsystemFacade.createCreditCard(name, exp, num, type);
    				
    			
    			//load cart
    			cartImpl.setCartId((new Integer(rs.getInt("shopcartid")).toString()));
    			cartImpl.setShipAddress(shippingAddress);
    			cartImpl.setBillAddress(billingAddress);
    			cartImpl.setPaymentInfo(creditCard);
    			
    			
    		}
    	}
    	catch(SQLException e){
            throw new DatabaseException(e);
        } 
    	
    }
    private void populateCartItemsList(ResultSet rs) throws BackendException {
    	CartItem cartItem = null;
        cartItemsList= new LinkedList<CartItem>();
        try {
            while(rs.next()){
                cartItem = new CartItemImpl(rs.getInt("shopcartid"),
                                        rs.getInt("productid"),
                                        rs.getInt("cartitemid"),
                                        makeString(rs.getInt("quantity")),
                                        makeString(rs.getDouble("totalprice")),
                                        true);
                
                cartItemsList.add(cartItem);
            }
        }
        catch(SQLException e){
            throw new BackendException(e);
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
	        case GET_ID:
	        	return getIdQuery;
	        case GET_SAVED_ITEMS:
	        	return getSavedItemsQuery;
	        case GET_TOP_LEVEL_SAVED_CART:
	        	return getTopLevelSavedCartQuery;
	        case SAVE_CART: 
	        	return saveCartQuery;
	        case SAVE_CART_ITEM:
	        	return saveCartItemQuery;
	        case DELETE_CART: 
	        	return deleteCartQuery;
	        case DELETE_ALL_CART_ITEMS:
	        	return deleteAllCartItemsQuery;
	        default:
	        	LOG.warning("Method getQuery() unexpectedly returning null");
	        	return null;	
	    }
    }
    @Override
    public Object[] getQueryParams() {
    	switch(queryType) {
	        case GET_ID:
	        	return getIdParams;
	        case GET_SAVED_ITEMS:
	        	return getSavedItemsParams;
	        case GET_TOP_LEVEL_SAVED_CART:
	        	return getTopLevelSavedCartParams;
	        case SAVE_CART: 
	        	return saveCartParams;
	        case SAVE_CART_ITEM:
	        	return saveCartItemParams;
	        case DELETE_CART: 
	        	return deleteCartParams;
	        case DELETE_ALL_CART_ITEMS:
	        	return deleteAllCartItemsParams;
	        default:
	        	LOG.warning("Method getQueryParams() unexpectedly returning null");
	        	return null;	        		
	        }
    }
    @Override
    public int[] getParamTypes() {
    	switch(queryType) {
        case GET_ID:
        	return getIdTypes;
        case GET_SAVED_ITEMS:
        	return getSavedItemsTypes;
        case GET_TOP_LEVEL_SAVED_CART:
        	return getTopLevelSavedCartTypes;
        case SAVE_CART: 
        	return saveCartTypes;
        case SAVE_CART_ITEM:
        	return saveCartItemTypes;
        case DELETE_CART: 
        	return deleteCartTypes;
        case DELETE_ALL_CART_ITEMS:
        	return deleteAllCartItemsTypes;
        default:
        	LOG.warning("Method getParamTypes() unexpectedly returning null");
        	return null;	
        }
    }
    
}
