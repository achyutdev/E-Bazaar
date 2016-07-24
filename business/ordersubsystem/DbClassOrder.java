
package business.ordersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

//import com.mysql.jdbc.PreparedStatement;

import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.util.Convert;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;
import business.productsubsystem.ProductSubsystemFacade;



class DbClassOrder implements DbClass,DbClassOrderForTest {
	enum Type {GET_ORDER_ITEMS, GET_ORDER_IDS, GET_ORDER_DATA, SUBMIT_ORDER, SUBMIT_ORDER_ITEM};

	private static final Logger LOG =
		Logger.getLogger(DbClassOrder.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS =
    	new DataAccessSubsystemFacade();

    private Type queryType;

    private String orderItemsQuery = "SELECT * FROM OrderItem WHERE orderid = ?";
    private String orderIdsQuery = "SELECT orderid FROM Ord WHERE custid = ?";
    private String orderDataQuery = "SELECT orderid, orderdate, totalpriceamount FROM Ord WHERE orderid = ?";
    private String submitOrderQuery = "INSERT into Ord "+
        "(custid, shipaddress1, shipcity, shipstate, shipzipcode, billaddress1, billcity, billstate,"+
           "billzipcode, nameoncard,  cardnum,cardtype, expdate, orderdate, totalpriceamount,totalshipmentcost,"
           +"totaltaxamount,totalamountcharged ) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private String submitOrderItemQuery ="INSERT into "
    		+ "orderItem(orderid,productid,quantity,totalprice,shipmentcost,taxamount) "
    		+ "VALUES(?,?,?,?,?,?)";
    Object[] orderItemsParams, orderIdsParams, orderDataParams, submitOrderParams, submitOrderItemParams;
    int[] orderItemsTypes, orderIdsTypes, orderDataTypes, submitOrderTypes, submitOrderItemTypes;

    //This is set by submitOrder and then used by submitOrderData
    private CustomerProfile custProfile;
    private List<Integer> orderIds;
    private List<OrderItem> orderItems;
    private OrderImpl orderData;
    private Order order;

    private List<Order> orders;

    DbClassOrder(){}

    List<Integer> getAllOrderIds(CustomerProfile custProfile) throws DatabaseException {
        queryType = Type.GET_ORDER_IDS;
        orderIdsParams = new Object[]{custProfile.getCustId()};
        orderIdsTypes = new int[]{Types.INTEGER};
        dataAccessSS.atomicRead(this);
        return Collections.unmodifiableList(orderIds);
    }

    OrderImpl getOrderData(Integer orderId) throws DatabaseException {
    	queryType = Type.GET_ORDER_DATA;
    	orderDataParams = new Object[]{orderId};
    	orderDataTypes = new int[]{Types.INTEGER};
    	dataAccessSS.atomicRead(this);
        return orderData;
    }

    /**
	 *  This method submits top-level data in Order to the Ord table (this is
	 *  executed within the helper method submitOrderData)
	 *  and then, after it gets the order id, it submits each OrderItem from
	 *  Order to the OrderItem table (items are submitted one at a time
	 *  using submitOrderItem). All this is done within a transaction.
	 *  Separate methods are provided
     */
    public void submitOrder(CustomerProfile custProfile, Order order) throws DatabaseException {
    	try	{
    		System.out.println("DbclassOrder: submitOrder.........."+order);
    		this.order=order;
        	this.custProfile=custProfile;
        	dataAccessSS.establishConnection(this);
        	dataAccessSS.startTransaction();
        	int orderid=submitOrderData();
        	System.out.println("DbclassOrder:submitorderpassed with ..........orderid:"+orderid);
        	for(OrderItem i:order.getOrderItems()){
        		i.setOrderId(orderid);
        		submitOrderItem(i);
        		System.out.println("submitorderitem pass++");
        	}
        	dataAccessSS.commit();
    	}
    	catch (DatabaseException e){
    		System.out.println("error in dbclass order:"+e.getMessage());
    		dataAccessSS.rollback();

    		throw e;
    	}


    	//LOG.warning("Method submitOrder(CustomerProfile custProfile, Order order) has not beenimplemented");
    	//implement
    }

    /** This is part of the general submitOrder method */
	private Integer submitOrderData() throws DatabaseException {
		queryType = Type.SUBMIT_ORDER;
		Address shipAddr = order.getShipAddress();
        Address billAddr = order.getBillAddress();
        CreditCard cc = order.getPaymentInfo();
    	submitOrderParams = new Object[]{custProfile.getCustId(),
    	                  shipAddr.getStreet(),
    	                  shipAddr.getCity(),
    	                  shipAddr.getState(),
    	                  shipAddr.getZip(),
    	                  billAddr.getStreet(),
    	                  billAddr.getCity(),
    	                  billAddr.getState(),
    	                  billAddr.getZip(),
    	                  cc.getNameOnCard(),
    	                  cc.getCardNum(),
    	                  cc.getCardType(),
    	                  cc.getExpirationDate(),
    	                  Convert.localDateAsString(order.getOrderDate()),
    	                  order.getTotalPrice(),
    	                  order.getTotalShippingCharge(),
    	                  order.getTotalTax(),
    	                  order.getTotalAmountCharged()};

    	submitOrderTypes = new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,//shipping
    			Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,//billing
    			Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,//cc
    			Types.VARCHAR, Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE};
		//creation and release of connection handled by submitOrder
    	//this should be part of a transaction started in submitOrder
		return dataAccessSS.insert();
	}

	/** This is part of the general submitOrder method */
	private void submitOrderItem(OrderItem item) throws DatabaseException {
        queryType=Type.SUBMIT_ORDER_ITEM;
       // LOG.warning("Method submitOrderItem(OrderItem item) in DbClassOrder has not been implemented.");
        //implement
        int orderid=item.getOrderId();
        int productid=item.getProductId();
        int qty=item.getQuantity();
        double totalprice=item.getTotalPrice();
        double shipmentcost=item.getShippingCost();
        double taxAmount=item.getTaxAmount();
        //System.out.println("orderid:"+orderid+"productid:"+productid+"qunatity:"+qty+"sjipmentCost:"+shipmentcost+"tax:"+taxAmount);
        submitOrderItemParams = new Object[]{
        		orderid,productid,qty,totalprice,shipmentcost,taxAmount
        };

        submitOrderItemTypes = new int[]{
        		Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE
        };

        dataAccessSS.insert();

	}

    public List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
    	System.out.println("inside getorderitems item: ");
    	//DbClassOrder cls=new DbClassOrder();
    	queryType=Type.GET_ORDER_ITEMS;
    	orderItemsParams=new Object[]{orderId};
    	orderItemsTypes=new int[]{Types.INTEGER};
    	dataAccessSS.atomicRead(this);
    	//implement
    	//LOG.warning("Method getOrderItems(Integer orderId) has not been implmeented");
        //orderItems = new ArrayList<>();
    	System.out.println("inside getorderitems item: ");
    	System.out.println("inside getorderitems item: Null check..."+orderItems.toString());
        return Collections.unmodifiableList(orderItems);
    }

    private void populateOrderItems(ResultSet rs) throws DatabaseException {
    	System.out.println("inside populatorder item: ");
    	orderItems=new ArrayList<OrderItem>();
    	try{
	    	while(rs.next()){
	    		int productid=rs.getInt("productid");
	    		int quantity=rs.getInt("quantity");
	    		//double totalPrice=rs.getDouble("totalprice");
	    		ProductSubsystem psf=new ProductSubsystemFacade();

	    		Product p=psf.getProductFromId(productid);
	    		String productname=p.getProductName();
                double unitPrice= p.getUnitPrice();
	    		OrderItem item=new OrderItemImpl(productname,quantity,unitPrice);
	    		orderItems.add(item);

	    	}
    	//LOG.warning("Method populateOrderItems(ResultSet) still needs to be implemented");
    	}
    	catch(SQLException |BackendException e){
    		throw new DatabaseException(e);
    	}

    }

    private void populateOrderIds(ResultSet resultSet) throws DatabaseException {
        orderIds = new LinkedList<Integer>();
        try {
            while(resultSet.next()){
                orderIds.add(resultSet.getInt("orderid"));
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
    }

    private void populateOrderData(ResultSet resultSet) throws DatabaseException {
    	orderData=new OrderImpl();
    	try {
			while(resultSet.next()){
				int orderid=resultSet.getInt("orderid");
				String orderDate=resultSet.getString("orderdate");
				Double totalPrice=resultSet.getDouble("totalpriceamount");
				List<OrderItem> items=getOrderItems(orderid);
				orderData.setOrderItems(items);
				orderData.setOrderId(orderid);
				orderData.setDate(Convert.localDateForString(orderDate));
				//orders.add(order);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}


    	//implement
    	//LOG.warning("Method populateOrderData(ResultSet resultSet) still needs to be implemented");
    }

    public void populateEntity(ResultSet resultSet) throws DatabaseException {
    	System.out.println("getorder items is going to populate....but which?");
    	switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		System.out.println("getorder items is going to call populate orderitms");
	    		populateOrderItems(resultSet);
	    		break;
	    	case GET_ORDER_IDS:
	    		populateOrderIds(resultSet);
	    		break;
	    	case GET_ORDER_DATA :
	        	populateOrderData(resultSet);
	        	break;
	        default :
	        	//do nothing
    	}
    }

    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    }

    public String getQuery() {
    	switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		return orderItemsQuery;
	    	case GET_ORDER_IDS:
	    		return orderIdsQuery;
	    	case GET_ORDER_DATA :
	        	return orderDataQuery;
	    	case SUBMIT_ORDER:
	    		return submitOrderQuery;
	    	case SUBMIT_ORDER_ITEM:
	    		return submitOrderItemQuery;
	        default :
	        	return null;
		}
    }

	@Override
	public Object[] getQueryParams() {
		switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		return orderItemsParams;
	    	case GET_ORDER_IDS:
	    		return orderIdsParams;
	    	case GET_ORDER_DATA :
	        	return orderDataParams;
	    	case SUBMIT_ORDER:
	    		return submitOrderParams;
	    	case SUBMIT_ORDER_ITEM:
	    		return submitOrderItemParams;
	        default :
	        	return null;
		}
	}

	@Override
	public int[] getParamTypes() {
		switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		return orderItemsTypes;
	    	case GET_ORDER_IDS:
	    		return orderIdsTypes;
	    	case GET_ORDER_DATA :
	        	return orderDataTypes;
	    	case SUBMIT_ORDER:
	    		return submitOrderTypes;
	    	case SUBMIT_ORDER_ITEM:
	    		return submitOrderItemTypes;
	        default :
	        	return null;
		}
	}



}
