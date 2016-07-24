package business.usecasecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import business.BusinessConstants;
import business.SessionCache;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;

public enum CheckoutController  {
	INSTANCE;

	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());


	public void runShoppingCartRules() throws RuleException, BusinessException {
		System.out.println("CheckoutCOntroller-runshoppingcartarules-started");
		ShoppingCartSubsystemFacade.INSTANCE.runShoppingCartRules();

	}

	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
			cust.runPaymentRules(addr, cc);
	}

	public Address runAddressRules(Address addr) throws RuleException, BusinessException {
		CustomerSubsystem cust =
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		return cust.runAddressRules(addr);
	}

	public List<Address> getShippingAddresses() throws BackendException {
		//implement
		System.out.println("inside Chedkoucontrol getshippinaddress............");
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		List<Address> shippingAddress= cust.getAllAddresses().stream()
				.filter(c->c.isShippingAddress())
				.collect(Collectors.toList());
		System.out.println("returned from checkoutcontroller");
		//System.out.println("shippingAddresslistsize:"+shippingAddress.size());
		return shippingAddress;
	}

	public List<Address> getBillingAddresses() throws BackendException {
		//implement
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		List<Address> shippingAddress= cust.getAllAddresses().stream()
				.filter(c->c.isBillingAddress())
				.collect(Collectors.toList());
		return shippingAddress;
	}

	//return default shipping address which was already loaded to cache in cus subsystem(added by nabin)
	public Address getDefaultShippingAddresses() throws BackendException {
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		Address defShipping= cust.getDefaultShippingAddress();
		System.out.print("check:..................................."+defShipping.getCity());
		return defShipping;
	}
	//return default billing address which was already loaded to cache in cus subsystem(added by nabin)
	public Address getDefaultBillingAddresses() throws BackendException {
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		Address defBilling= cust.getDefaultBillingAddress();
		return defBilling;
	}
	public CreditCard getDefaultPayment() throws BackendException {
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		CreditCard defCreditCard= cust.getDefaultPaymentInfo();
		return defCreditCard;
	}
	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		scss.runFinalOrderRules();
	}

	/** Asks Customer Subsystem to check credit card against
	 *  Credit Verification System
	 */
	public void verifyCreditCard() throws BusinessException {
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		cust.checkCreditCard();
	}

	public void saveNewAddress(Address addr) throws BackendException {
		CustomerSubsystem cust =
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		cust.saveNewAddress(addr);
	}

	/** Asks Customer Subsystem to submit final order
	 * @throws BusinessException
	 * @throws RuleException */
	public void submitFinalOrder() throws RuleException, BusinessException {
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);

		runFinalOrderRules(cust.getShoppingCart());//first run rules

		//OrderSubsystem order= new OrderSubsystemFacade(cust.getCustomerProfile());
		//order.submitOrder(cust.getShoppingCart().getLiveCart());
		cust.submitOrder();
	}
	public void prepareShoppingCartItemsForCheckout(List<CartItem> ci){
		ShoppingCartSubsystem ss= ShoppingCartSubsystemFacade.INSTANCE;
		ss.updateShoppingCartItems(ci);

	}

	public void prepareShippingandBillingInfoForCheckout(Address ship, Address bill){
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		cust.setShippingAddressInCart(ship);
		cust.setBillingAddressInCart(bill);

	}

	public void prepareCreditCardInfoForCheckout(CreditCard cc){
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		cust.setPaymentInfoInCart(cc);

	}

	//return alternate adress for ship bill of the customer for selecting new window
	//public


}
