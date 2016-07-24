package business.ordersubsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.productsubsystem.ProductSubsystemFacade;

public class OrderSubsystemFacade implements OrderSubsystem {
	private static final Logger LOG =
			Logger.getLogger(OrderSubsystemFacade.class.getPackage().getName());
	CustomerProfile custProfile;

    public OrderSubsystemFacade(CustomerProfile custProfile){
        this.custProfile = custProfile;
    }

	/**
     *  Used by customer subsystem at login to obtain this customer's order history from the database.
	 *  Assumes cust id has already been stored into the order subsystem facade
	 *  This is created by using auxiliary methods at the bottom of this class file.
	 *  First get all order ids for this customer. For each such id, get order data
	 *  and form an order, and with that order id, get all order items and insert
	 *  into the order.
	 */
    public List<Order> getOrderHistory() throws BackendException {
    	//LOG.warning("Method getOrderHistory() still needs to be implemented");
    	List<Order> orders=new ArrayList<>();
    	try {
    		System.out.println("inside getorderhistory ");
			List<Integer> orderIds=getAllOrderIds();
			System.out.println("orderid list: "+orderIds.toString());
			for(Integer orderid:orderIds){
				OrderImpl odr=getOrderData(orderid);
				orders.add(odr);
			}
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
    	return orders;
    }

    public void submitOrder(ShoppingCart cart) throws BackendException {
    	System.out.println("orderSubsytem: submitOrder..........");
    	DbClassOrder dbClass = new DbClassOrder();
    	List<OrderItem> orderItems=new ArrayList<>();
    	for(CartItem item:cart.getCartItems()){
    		orderItems.add(convertCartItemToOrder(item));
    	}
    	OrderImpl ord=new OrderImpl();
    	ord.setOrderItems(orderItems);
    	ord.setShippAdress(cart.getShippingAddress());
    	System.out.println("orderSubsytem:submitOrder:i got shipping address.....");
    	ord.setBillAddress(cart.getBillingAddress());
    	ord.setPaymentCard(cart.getPaymentInfo());
    	ord.setDate(LocalDate.now());
    	try {
			dbClass.submitOrder(this.custProfile, ord);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}

    	//LOG.warning("The method submitOrder(ShoppingCart cart) in OrderSubsystemFacade has not been implemented");
    }

	/** Used whenever an order item needs to be created from outside the order subsystem */
    public static OrderItem createOrderItem(
    		Integer prodId, Integer orderId, String quantityReq, String totalPrice) throws BackendException{
    	//implement
//        LOG.warning("Method createOrderItem(prodid, orderid, quantity, totalprice) still needs to be implemented");
//    	return null;
    	ProductSubsystemFacade prodSS=new ProductSubsystemFacade();
    	OrderItemImpl order;
    	try {
			order=new OrderItemImpl(prodSS.getProductFromId(prodId).getProductName(),
			Integer.parseInt(quantityReq), Double.parseDouble(totalPrice));
			order.setOrderId(orderId);
		} catch (BackendException e) {
			throw new BackendException(e);
		}
    	return order;
    }

    /** to create an Order object from outside the subsystem
     * @throws DatabaseException */
    public static Order createOrder(Integer orderId, String orderDate, String totalPrice) throws BackendException {
    	OrderImpl order=new OrderImpl();
    	DbClassOrder dbClass = new DbClassOrder();
    	List<OrderItem> items;
		try {
			items = dbClass.getOrderItems(orderId);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
    	order.setOrderItems(items);
    	order.setOrderId(orderId);
    	//implement
       // LOG.warning("Method  createOrder(Integer orderId, String orderDate, String totalPrice) still needs to be implemented");
    	return order;
    }

    public static Order createFakeGenericOrder(){
    	OrderImpl order=new OrderImpl();
    	order.setDate(LocalDate.now());
        OrderItem item = createFakeOrderITem("Pants",1,10000.123);
        List<OrderItem> items= new ArrayList<OrderItem>();
         items.add(item);
         order.setOrderItems(items);

         Address shipAddr= CustomerSubsystemFacade.createAddress("100", "fakeShip", "test", "test", true, false);
         Address billAddr= CustomerSubsystemFacade.createAddress("100", "fakeBill", "test", "test", false, true);
         CreditCard cc= CustomerSubsystemFacade.createCreditCard("Nabin", "2050/02/02", "1234567891234567", "VISA");

         order.setBillAddress(billAddr);
         order.setShippAdress(shipAddr);
         order.setPaymentCard(cc);


    	return order;
    }

    public static OrderItem createFakeOrderITem(String name,int quantity, double price){
    	OrderItem item = new OrderItemImpl(name, quantity,price);
    	return item;

    }

    ///////////// Methods internal to the Order Subsystem -- NOT public
    List<Integer> getAllOrderIds() throws DatabaseException {
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getAllOrderIds(custProfile);

    }

    /** Part of getOrderHistory */
    List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getOrderItems(orderId);
    }

    /** Uses cust id to locate top-level order data for customer -- part of getOrderHistory */
    OrderImpl getOrderData(Integer orderId) throws DatabaseException {
    	DbClassOrder dbClass = new DbClassOrder();
    	return dbClass.getOrderData(orderId);
    }

    public OrderItem convertCartItemToOrder(CartItem item){
    	OrderItem odrItem= new OrderItemImpl(item.getProductName(),
				Integer.parseInt(item.getQuantity()),
				Double.parseDouble(item.getTotalprice()));
		odrItem.setProductId(item.getProductid());
		return odrItem;
    }
    public static DbClassOrderForTest createGenericDbClassOrderForTest(){
    	return new DbClassOrder();
    }
}
