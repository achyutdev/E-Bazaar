package presentation.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import business.BusinessConstants;
import business.SessionCache;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.usecasecontrol.BrowseAndSelectController;
import business.usecasecontrol.CheckoutController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.gui.GuiConstants;
import presentation.util.UtilForUIClasses;

public enum CheckoutData {
	INSTANCE;

	public Address createAddress(String street, String city, String state,
			String zip, boolean isShip, boolean isBill) {
		return CustomerSubsystemFacade.createAddress(street, city, state, zip, isShip, isBill);
	}

	public CreditCard createCreditCard(String nameOnCard,String expirationDate,
               String cardNum, String cardType) {
		return CustomerSubsystemFacade.createCreditCard(nameOnCard, expirationDate,
				cardNum, cardType);
	}

	//Customer Ship Address Data
	private ObservableList<CustomerPres> shipAddresses ;

	//Customer Bill Address Data
	private ObservableList<CustomerPres> billAddresses ;
	/*
	private List<CustomerPres> shipInfoForCust() {
		//go to use case controller
		//get saved ship addresses for customer
		//get cust profile
		//assemble into a List<CustomerPres> and return
	}*/
	private ObservableList<CustomerPres> loadShipAddresses(){
		System.out.println("/nm on defaultdataOfcustPRes..on..on..on./............");
	   List<Address> shipAdresses;
	   List<CustomerPres> list = new ArrayList<CustomerPres>();
	try {
		shipAdresses = CheckoutController.INSTANCE.getShippingAddresses();
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
	   CustomerProfile customer= cust.getCustomerProfile();

	   CustomerPres newCust;
	   for(Address a:shipAdresses ){
		   newCust= new CustomerPres(customer,a);
		   list.add(newCust);
	   	}

		}
	   catch (BackendException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	System.out.println("returned from chekout data load shipaddress");
	return FXCollections.observableList(list);
	}

	private ObservableList<CustomerPres> loadBillAddresses() {
		List<Address> billAdresses;
		List<CustomerPres> list = new ArrayList<CustomerPres>();
		try {
			billAdresses = CheckoutController.INSTANCE.getBillingAddresses();


			   CustomerSubsystem cust =
						(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
			   CustomerProfile customer= cust.getCustomerProfile();

			   CustomerPres newCust;
			   for(Address a:billAdresses ){
				   newCust= new CustomerPres(customer,a);
				   list.add(newCust);
			   }


		}
		catch (BackendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FXCollections.observableList(list);
	}
	public ObservableList<CustomerPres> getCustomerShipAddresses() {
		shipAddresses= loadShipAddresses();
		return shipAddresses;
	}
	public ObservableList<CustomerPres> getCustomerBillAddresses() {
		billAddresses= loadBillAddresses();
		return billAddresses;
	}
	public List<String> getDisplayAddressFields() {
		return GuiConstants.DISPLAY_ADDRESS_FIELDS;
	}
	public List<String> getDisplayCredCardFields() {
		return GuiConstants.DISPLAY_CREDIT_CARD_FIELDS;
	}
	public List<String> getCredCardTypes() {
		return GuiConstants.CREDIT_CARD_TYPES;
	}
	public Address getDefaultShippingData() throws BackendException {

		return CheckoutController.INSTANCE.getDefaultShippingAddresses();

		/*List<String> add = DefaultData.DEFAULT_SHIP_DATA;
		return CustomerSubsystemFacade.createAddress(add.get(0), add.get(1),
				add.get(2), add.get(3), true, false);*/
	}

	public Address getDefaultBillingData() throws BackendException {
		 return CheckoutController.INSTANCE.getDefaultBillingAddresses();
		/*List<String> add =  DefaultData.DEFAULT_BILLING_DATA;
		return CustomerSubsystemFacade.createAddress(add.get(0), add.get(1),
				add.get(2), add.get(3), false, true);*/
	}

	public List<String> getDefaultPaymentInfo() throws BackendException {

		CreditCard defaultPaymentInfo= CheckoutController.INSTANCE.getDefaultPayment();
		List<String> defaultValues= new ArrayList<String>();
		defaultValues.add(defaultPaymentInfo.getNameOnCard());
		defaultValues.add(defaultPaymentInfo.getCardNum());
		defaultValues.add(defaultPaymentInfo.getCardType());
		defaultValues.add(defaultPaymentInfo.getExpirationDate());
		return defaultValues;
	}


	public CustomerProfile getCustomerProfile() {
		CustomerSubsystem cust =
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		return cust.getCustomerProfile();
				//BrowseAndSelectController.INSTANCE.getCustomerProfile();
	}



	private class ShipAddressSynchronizer implements Synchronizer {
		public void refresh(ObservableList list) {
			shipAddresses = list;
		}
	}
	public ShipAddressSynchronizer getShipAddressSynchronizer() {
		return new ShipAddressSynchronizer();
	}

	private class BillAddressSynchronizer implements Synchronizer {
		public void refresh(ObservableList list) {
			billAddresses = list;
		}
	}
	public BillAddressSynchronizer getBillAddressSynchronizer() {
		return new BillAddressSynchronizer();
	}
	public void submitOrder() throws RuleException, BusinessException{
		CheckoutController.INSTANCE.submitFinalOrder();
	}

	public void deleteAddress(ObservableList<CustomerPres> selectedItems){

	}
	public void prepareCartTocheckout(List<CartItemPres> itemsList){
	  CheckoutController.INSTANCE.prepareShoppingCartItemsForCheckout(UtilForUIClasses.cartItemPresToCartItemList(itemsList));
  }

	public void PrepareShipandBillAddressForCheckout(Address ship, Address bill){
		CheckoutController.INSTANCE.prepareShippingandBillingInfoForCheckout(ship, bill);
	}

	public void PrepareCreditCartForCheckout(CreditCard cc){
		CheckoutController.INSTANCE.prepareCreditCardInfoForCheckout(cc);
	}
	public static class ShipBill {
		public boolean isShipping;
		public String label;
		public Synchronizer synch;
		public ShipBill(boolean shipOrBill, String label, Synchronizer synch) {
			this.isShipping = shipOrBill;
			this.label = label;
			this.synch = synch;
		}

	}

}
