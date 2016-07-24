
package business.externalinterfaces;

import java.util.List;

import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;


public interface ShoppingCartSubsystem {
	/**
	 *  Used during customer login -- when login succeeds, customer profile
     *  is set in shopping cart subsystem
     */
	public void setCustomerProfile(CustomerProfile custProfile);

	/**
	 *  Used during customer login to cache this customer's saved cart -- performs
	 *  a database access through data access subsystem
	 */
	public void retrieveSavedCart() throws BackendException ;


	public void updateShoppingCartItems(List<CartItem> list);

	public void makeSavedCartLive();


	public ShoppingCart getLiveCart();

	public List<CartItem> getCartItems();

	 //static methods


    //tests
   public ShoppingCart getEmptyCartForTest();
   public CartItem getEmptyCartItemForTest();



 	/**
 	 * Empties the live cart. This needs to be done after an order has been submitted
 	 */
 	public void clearLiveCart();


 	/**Used to display items currently in live shopping cart (for instance, when an item is added to cart
 	 * and the CartItemsWindow is about to be displayed). the method returns the list of cart items currently
 	 * stored in the live cart, sitting in the shopping cart subsystem facade */
 	public List<CartItem> getLiveCartItems();

 	/**
 	 * Accessor used by customer subsystem to store user's selected ship address during checkout;
 	 * stores value in shop cart facade
 	 */
 	public void setShippingAddress(Address addr);

 	/**
 	 * Accessor used by customer subsystem to store user's selected ship address during checkout;
 	 * stores value in shop cart facade
 	 */
 	public void setBillingAddress(Address addr);
 	/**
 	 * Accessor used by customer subsystem to store user's selected ship address during checkout;
 	 * stores value in shop cart facade
 	 */
 	public void setPaymentInfo(CreditCard cc);



 	/**
 	 * Used when user choose the option to 'retrieve saved cart' -- which
 	 * requires that the customer's saved cart be stored in the live cart
 	 * in the shopping cart subsystem facade
 	 */
 	//public void makeSavedCartLive();

 	/** used when user selects the "Save Cart" option on the cart items window */
 	public void saveLiveCart() throws BackendException;

       /**
        *  Used when a user enters Checkout use case by
        *  clicking "Proceed to Checkout" -- at that time
        *  rules concerning validity of shopping cart are run
        *  (for instance, user may not have an empty cart)
        */
 	public void runShoppingCartRules() throws RuleException, BusinessException;

 	/**
        *  Invoked when user attempts to submit final order
        *  -- rules are run to check validity of the order
        *  (for example, quantity available for each product order
        *  will be checked against quantity requested)
        */
   public void runFinalOrderRules() throws RuleException, BusinessException;
}
