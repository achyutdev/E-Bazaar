package business.usecasecontrol;


import java.util.List;

import presentation.gui.ShoppingCartWindow;
import business.RulesQuantity;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.RulesShoppingCart;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.DataUtil;

public enum BrowseAndSelectController {
	INSTANCE;
	
	public void updateShoppingCartItems(List<CartItem> cartitems) {
		ShoppingCartSubsystemFacade.INSTANCE.updateShoppingCartItems(cartitems);
	}
	
	public List<CartItem> getCartItems() {
		return ShoppingCartSubsystemFacade.INSTANCE.getCartItems();
	}
	
	/** Makes saved cart live in subsystem and then returns the new list of cartitems */
	public void retrieveSavedCart() {
		ShoppingCartSubsystem shopCartSS = ShoppingCartSubsystemFacade.INSTANCE;
		
		// Saved cart was retrieved during login
		shopCartSS.makeSavedCartLive();	
	}
	
	
	public void SavedCart() {
		CustomerSubsystem customer 
		= DataUtil.readCustFromCache();
				CustomerProfile customerProfile = customer.getCustomerProfile();
		//ShoppingCartSubsystem shop = customer.getShoppingCart();
				ShoppingCartSubsystem shop = ShoppingCartSubsystemFacade.INSTANCE;
		shop.setCustomerProfile(customerProfile);
		shop.getLiveCart().setShipAddress(customer.getDefaultShippingAddress());
		shop.getLiveCart().setBillAddress(customer.getDefaultBillingAddress());
		shop.getLiveCart().setPaymentInfo(customer.getDefaultPaymentInfo());
		
		System.out.println("inside browse and sselect...checking if cust null=="+customerProfile.getFirstName());
		System.out.println("inside browse and sselect...checking if livecart null=="+shop.getLiveCart().getShippingAddress().getCity());
		ShoppingCart liveCart = shop.getLiveCart();
		boolean rulesOK =false;
	
		Rules rules = new RulesShoppingCart(liveCart);
		try {
			rules.runRules();
		} catch (RuleException re) {
			String errMsg = "Rules dosn't match: " + re.getMessage();
			System.out.println("rules " + errMsg);
			rulesOK = true;
		} catch (BusinessException ee) {
			String errMsg = "BusinessException : " + ee.getMessage();
			System.out.println("Busiexception " + errMsg);

			rulesOK = true;
		}
		
		if (rulesOK == false)
			try {
				System.out.println("save cart in progress.....................");
			
			shop.saveLiveCart();
				ShoppingCartWindow.INSTANCE.displayInfo("Your cart has saved");

			} catch (BackendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void runQuantityRules(Product product, String quantityRequested)
			throws RuleException, BusinessException {

		ProductSubsystem prodSS = new ProductSubsystemFacade();
		
		//find current quant avail since quantity may have changed
		//since product was first loaded into UI
		int currentQuantityAvail = prodSS.readQuantityAvailable(product);
		Rules transferObject = new RulesQuantity(currentQuantityAvail, quantityRequested);//
		transferObject.runRules();

	}
	
	public List<Catalog> getCatalogs() throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getCatalogList();
	}
	
	public List<Product> getProducts(Catalog catalog) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductList(catalog);
	}
	public Product getProductForProductName(String name) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductFromName(name);
	}
	
	/** Assume customer is logged in */
	public CustomerProfile getCustomerProfile() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		return cust.getCustomerProfile();
	}
}
