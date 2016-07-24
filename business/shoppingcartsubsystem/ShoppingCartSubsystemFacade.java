package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import middleware.exceptions.DatabaseException;

public enum ShoppingCartSubsystemFacade implements ShoppingCartSubsystem {
	INSTANCE;

	ShoppingCartImpl liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
	ShoppingCartImpl savedCart;
	Integer shopCartId;
	CustomerProfile customerProfile;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());

	// interface methods
	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}

	public void makeSavedCartLive() {
		liveCart = savedCart;
	}

	public ShoppingCart getLiveCart() {
		return liveCart;
	}

	public void runShoppingCartRules() throws RuleException, BusinessException {
		System.out.println("ShoppingCartSubsystemFacade-runshoppingcartarules-started");
		Rules transferObject = new RulesShoppingCart(liveCart);
		transferObject.runRules();
	}

	public void retrieveSavedCart() throws BackendException {
		try {
			DbClassShoppingCart dbClass = new DbClassShoppingCart();
			ShoppingCartImpl cartFound = dbClass.retrieveSavedCart(customerProfile);
			if(cartFound == null) {
				savedCart = new ShoppingCartImpl(new ArrayList<CartItem>());
			} else {
				savedCart = cartFound;
			}
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}

	}



	public void updateShoppingCartItems(List<CartItem> list) {
		liveCart.setCartItems(list);
	}

	public List<CartItem> getCartItems() {
		return liveCart.getCartItems();
	}

	//static methods
	public static CartItem createCartItem(String productName, String quantity,
            String totalprice) {
		try {
			return new CartItemImpl(productName, quantity, totalprice);
		} catch(BackendException e) {
			throw new RuntimeException("Can't create a cartitem because of productid lookup: " + e.getMessage());
		}
	}
	//static methods
		public static ShoppingCart createFakeShoppingCart(String productName, String quantity,
	            String totalprice) {
			try {
				ShoppingCart a= new ShoppingCartImpl();

				List<CartItem>  items =new ArrayList<CartItem>();
				items.add(createCartItem(productName, quantity, totalprice));
				a.setCartItems(items);
				a.setBillAddress(CustomerSubsystemFacade.createAddress("Street", "city", "state", "zip", false, true));
				a.setShipAddress(CustomerSubsystemFacade.createAddress("Street", "city", "state", "zip", true, false));
				a.setPaymentInfo(CustomerSubsystemFacade.createCreditCard("Test", "2015/01/01", "123456", "Visa"));
				return a;
			} catch(Exception e) {
				throw new RuntimeException("Can't create a cart because of Exception: " + e.getMessage());
			}
		}



	//interface methods for testing

	public ShoppingCart getEmptyCartForTest() {
		return new ShoppingCartImpl();
	}


	public CartItem getEmptyCartItemForTest() {
		return new CartItemImpl();
	}

	@Override
	public void clearLiveCart() {
		System.out.println("live cart cleared.................");
		liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
		// TODO Auto-generated method stub

	}

	@Override
	public List<CartItem> getLiveCartItems() {
		return liveCart.getCartItems();
	}

	@Override
	public void setShippingAddress(Address addr) {
		liveCart.setShipAddress(addr);

	}

	@Override
	public void setBillingAddress(Address addr) {
		liveCart.setBillAddress(addr);

	}

	@Override
	public void setPaymentInfo(CreditCard cc) {
		liveCart.setPaymentInfo(cc);

	}

	@Override
	public void saveLiveCart() throws BackendException {

			DbClassShoppingCart dbClass = new DbClassShoppingCart();
			 try {
				dbClass.saveCart(customerProfile, liveCart);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	@Override
	public void runFinalOrderRules() throws RuleException, BusinessException {
		Rules transferObject = new RulesFinalOrder(liveCart);
		transferObject.runRules();

	}

}
