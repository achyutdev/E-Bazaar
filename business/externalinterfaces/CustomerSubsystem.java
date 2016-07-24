
package business.externalinterfaces;
import java.util.List;

import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;

/**
 * Encapsulates the customer and the things the customers owns, like address, credit
 * card, shopping cart.
 *
 * Note: createAddress method is static, so does not appear on the interface
 */
public interface CustomerSubsystem {
	/** Use for loading order history,
	 * default addresses, default payment info,
	 * saved shopping cart,cust profile
	 * after login*/
    public void initializeCustomer(Integer id, int authorizationLevel) throws BackendException;

    /**
     * Returns true if user has admin access
     */
    public boolean isAdmin();


    /**
     * Use for saving an address created by user
     */
    public void saveNewAddress(Address addr) throws BackendException;

    /**
     * Use to supply all stored addresses of a customer when he wishes to select an
	 * address in ship/bill window. Requires a trip to the database.
	 */
    public List<Address> getAllAddresses() throws BackendException;

    /** Used whenever a customer name needs to be accessed, after login */
    public CustomerProfile getCustomerProfile();

    /** Used when ship/bill window is first displayed, after login */
    public Address getDefaultShippingAddress();

    /** Used when ship/bill window is first displayed, after login */
    public Address getDefaultBillingAddress();

    /** Used when payment window is first displayed (after login) */
    public CreditCard getDefaultPaymentInfo();
    /**

    /**
	 *  Runs address rules and returns the cleansed address.
     *  If a RuleException is thrown, this represents a validation error
     *  and the error message should be extracted from the exception and displayed
     */
    public Address runAddressRules(Address addr) throws RuleException, BusinessException;

  /**
   *  Runs payment rules;
   *  if a RuleException is thrown, this represents a validation error
   *  and the error message should be extracted and displayed
   */
  public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException;

  /**
	 * Customer Subsystem is responsible for obtaining all the data needed by
	 * Credit Verif system -- it does not (and should not) rely on the
	 * controller for this data.
	 */
	public void checkCreditCard() throws BusinessException;


    /**
     * Returns this customer's order history, stored in the Customer Subsystem (not
     * read from the database). Used by other subsystems
     * to read current user's order history (not used during login process)
     * */
    public List<Order> getOrderHistory();






    /**
     *  Stores address as shipping address in this customer's shopping cart
	 */
    public void setShippingAddressInCart(Address addr);

    /**
     * Stores address as billing address in this customer's shopping cart
	 */
    public void setBillingAddressInCart(Address addr);

    /** Stores credit card in this customer's shopping cart */
    public void setPaymentInfoInCart(CreditCard cc);


    /**
     *  Called when user submits final order -- customer sends its shopping cart to order subsystem
	 *  and order subsystem extracts items from shopping cart and prepares order
	 */
    public void submitOrder() throws BackendException;

    /**
     * After an order is submitted, the list of orders cached in CustomerSubsystemFacade
     * will be out of date; this method should cause order data to be reloaded
     */
    public void refreshAfterSubmit() throws BackendException;

    /**
	 * Used whenever the shopping cart needs to be displayed
	 */
    public ShoppingCartSubsystem getShoppingCart();

    /**
	 * Saves shopping cart to database
	 */
    public void saveShoppingCart() throws BackendException;





   // TESTING
    public DbClassAddressForTest getGenericDbClassAddress();
    public CustomerProfile getGenericCustomerProfile();

}
