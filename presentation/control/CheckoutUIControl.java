package presentation.control;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.data.BrowseSelectData;
import presentation.data.CheckoutData;
import presentation.data.CustomerPres;
import presentation.data.ErrorMessages;
import presentation.gui.CatalogListWindow;
import presentation.gui.FinalOrderWindow;
import presentation.gui.OrderCompleteWindow;
import presentation.gui.PaymentWindow;
import presentation.gui.ShippingBillingWindow;
import presentation.gui.ShoppingCartWindow;
import presentation.gui.TermsWindow;
//import rulesengine.OperatingException;
//import rulesengine.ReteWrapper;
//import rulesengine.ValidationException;
//import system.rulescore.rulesengine.*;
//import system.rulescore.rulesupport.*;
//import system.rulescore.util.*;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.usecasecontrol.CheckoutController;
import business.util.DataUtil;

public enum CheckoutUIControl {
	INSTANCE;

	private static final Logger LOG = Logger.getLogger(CheckoutUIControl.class
			.getPackage().getName());
	// Windows managed by CheckoutUIControl
	ShippingBillingWindow shippingBillingWindow;
	PaymentWindow paymentWindow;
	TermsWindow termsWindow;
	FinalOrderWindow finalOrderWindow;
	OrderCompleteWindow orderCompleteWindow;
	public ShippingBillingWindow getShippingBillingWindow() {
		return shippingBillingWindow;
	}

	// handler for ShoppingCartWindow proceeding to checkout
	private class ProceedFromCartToShipBill implements
			EventHandler<ActionEvent>, Callback {
		CheckoutData data;
		public void doUpdate(){
			try	{
				data = CheckoutData.INSTANCE;
				CustomerProfile custProfile = data.getCustomerProfile();
				Address defaultShipAddress = data.getDefaultShippingData();
				Address defaultBillAddress = data.getDefaultBillingData();
				data.prepareCartTocheckout(ShoppingCartWindow.INSTANCE.getCartItems());//-----//
				//Address defaultShipAddress = CheckoutController.INSTANCE.getDefaultShippingAddresses();
				//Address defaultBillAddress = CheckoutController.INSTANCE.getDefaultBillingAddresses();

				shippingBillingWindow.setShippingAddress(custProfile.getFirstName()
						+ " " + custProfile.getLastName(),
						defaultShipAddress.getStreet(),
						defaultShipAddress.getCity(),
						defaultShipAddress.getState(), defaultShipAddress.getZip());
				shippingBillingWindow.setBillingAddress(custProfile.getFirstName()
						+ " " + custProfile.getLastName(),
						defaultBillAddress.getStreet(),
						defaultBillAddress.getCity(),
						defaultBillAddress.getState(), defaultBillAddress.getZip());
				ShoppingCartWindow.INSTANCE.hide();
				shippingBillingWindow.show();
			}
			catch (BackendException e){
				LOG.warning("Backend Error in CheckoutUiControl");
			}

		}
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.clearMessages();
			ShoppingCartWindow.INSTANCE.setTableAccessByRow();


			boolean rulesOk = true;
			/* check that cart is not empty before going to next screen */

			try {
				System.out.println("Started Shopiingcartrules nabin..........................");
				CheckoutController.INSTANCE.runShoppingCartRules();
				System.out.println("passed Shopiingcartrules nabin");
			} catch (RuleException e) {
				System.out.println("catch rules");
				ShoppingCartWindow.INSTANCE.displayError("ShoppingCartRules:"+e.getMessage());
				rulesOk= false;
			} catch (BusinessException e) {
				System.out.println("catch businessexception");
				rulesOk=false;
			}

			if (rulesOk) {
				boolean isLoggedIn = DataUtil.isLoggedIn();
				shippingBillingWindow = new ShippingBillingWindow();
				if (!isLoggedIn) {
					LoginUIControl loginControl
					  = new LoginUIControl(shippingBillingWindow, ShoppingCartWindow.INSTANCE, this);
					loginControl.startLogin();
				} else {
					doUpdate();
				}
				//shippingBillingWindow.createPopups()
			}
			else{
				//ShoppingCartWindow.INSTANCE.show();
			}

		}
		@Override
		public Text getMessageBar() {
			return ShoppingCartWindow.INSTANCE.getMessageBar();
		}
	}
	public ProceedFromCartToShipBill getProceedFromCartToShipBill() {
		return new ProceedFromCartToShipBill();
	}

	// handlers for ShippingBillingWindow
	private class BackToShoppingCartHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			shippingBillingWindow.clearMessages();
			shippingBillingWindow.hide();
		}
	}

	public BackToShoppingCartHandler getBackToShoppingCartHandler() {
		return new BackToShoppingCartHandler();
	}

	private class ProceedToPaymentHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			shippingBillingWindow.clearMessages();
			boolean rulesOk = true;
			Address cleansedShipAddress = null;
			Address cleansedBillAddress = null;
				try {
					cleansedShipAddress = CheckoutController.INSTANCE.runAddressRules(shippingBillingWindow
							.getShippingAddress());
				} catch (RuleException e) {
					rulesOk = false;
					shippingBillingWindow
							.displayError("Shipping address error: "
									+ e.getMessage());
				} catch (BusinessException e) {
					rulesOk = false;
					shippingBillingWindow.displayError(
							ErrorMessages.GENERAL_ERR_MSG + ": Message: " + e.getMessage());
				}
			if (rulesOk) {
					try {
						cleansedBillAddress = CheckoutController.INSTANCE.runAddressRules(shippingBillingWindow
								.getBillingAddress());
					} catch (RuleException e) {
						rulesOk = false;
						shippingBillingWindow
								.displayError("Billing address error: "
										+ e.getMessage());
					} catch (BusinessException e) {
						rulesOk = false;
						shippingBillingWindow.displayError(
								ErrorMessages.GENERAL_ERR_MSG + ": Message: " + e.getMessage());
					}
			}
			if (rulesOk) {

				LOG.info("Address Rules passed!");
				if (cleansedShipAddress != null&& shippingBillingWindow.getSaveShipAddr()) {
					try {
						CheckoutController.INSTANCE.saveNewAddress(cleansedShipAddress);
					} catch(BackendException e) {
						shippingBillingWindow.displayError("New shipping address not saved. Message: "
							+ e.getMessage());
					}
				}
				if (cleansedBillAddress != null && shippingBillingWindow.getSaveBillAddr()) {
					try {
						CheckoutController.INSTANCE.saveNewAddress(cleansedBillAddress);
					} catch(BackendException e) {
						shippingBillingWindow.displayError("New billing address not saved. Message: "
							+ e.getMessage());
					}
				}
				
				Address ship=shippingBillingWindow.getShippingAddress();
				Address bill=shippingBillingWindow.getBillingAddress();
				CheckoutData.INSTANCE.PrepareShipandBillAddressForCheckout(ship, bill);
				paymentWindow = new PaymentWindow();
				paymentWindow.show();
				shippingBillingWindow.hide();
				
			}
		}
	}
	public List<String> getDefaultPaymentInfo(){
		List<String> list= new ArrayList<String>();
		try {
			 list=CheckoutData.INSTANCE.getDefaultPaymentInfo();
		} catch (BackendException e) {
			paymentWindow.displayError("Backend Exception:"+e.getMessage());
		}
		return list;
	}

	public ProceedToPaymentHandler getProceedToPaymentHandler() {
		return new ProceedToPaymentHandler();
	}

	public class SaveShipChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed,
				Boolean oldval, Boolean newval) {
			shippingBillingWindow.displayInfo("");
		}
	}

	public class SaveBillChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed,
				Boolean oldval, Boolean newval) {
			shippingBillingWindow.displayInfo("");

		}
	}

	public SaveShipChangeListener getSaveShipChangeListener() {
		return new SaveShipChangeListener();
	}

	public SaveBillChangeListener getSaveBillChangeListener() {
		return new SaveBillChangeListener();
	}

	// handlers for PaymentWindow
	private class BackToShipBillWindow implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			shippingBillingWindow.show();
			paymentWindow.hide();
		}
	}

	public BackToShipBillWindow getBackToShipBillWindow() {
		return new BackToShipBillWindow();
	}

	private class BackToCartHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			ShoppingCartWindow.INSTANCE.show();
			paymentWindow.hide();
		}
	}

	public BackToCartHandler getBackToCartHandler() {
		return new BackToCartHandler();
	}

	private class ProceedToTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			try {
				CheckoutController.INSTANCE.runPaymentRules(shippingBillingWindow.getBillingAddress(),
					paymentWindow.getCreditCardFromWindow());

				paymentWindow.clearMessages();
				paymentWindow.hide();

				CheckoutData.INSTANCE.PrepareCreditCartForCheckout(paymentWindow.getCreditCardFromWindow());
				CheckoutController.INSTANCE.verifyCreditCard();
				termsWindow = new TermsWindow();
				termsWindow.show();
			} catch(RuleException e) {
				paymentWindow.displayError(e.getMessage());
			} catch(BusinessException e) {
				paymentWindow.displayError("Error:"+e.getMessage());
			}
		}
	}

	public ProceedToTermsHandler getProceedToTermsHandler() {
		return new ProceedToTermsHandler();
	}


	// handlers for TermsWindow

	private class ToCartFromTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			termsWindow.hide();
			ShoppingCartWindow.INSTANCE.show();
		}
	}

	public ToCartFromTermsHandler getToCartFromTermsHandler() {
		return new ToCartFromTermsHandler();
	}

	private class AcceptTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow = new FinalOrderWindow();
			finalOrderWindow.setData(BrowseSelectData.INSTANCE.getCartData());
			finalOrderWindow.show();
			termsWindow.hide();
		}
	}

	public AcceptTermsHandler getAcceptTermsHandler() {
		return new AcceptTermsHandler();
	}

	// handlers for FinalOrderWindow
	private class SubmitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {

			try {
				System.out.println("inside submit order handler......");
				CheckoutData.INSTANCE.submitOrder();
				orderCompleteWindow = new OrderCompleteWindow();
				orderCompleteWindow.show();
				finalOrderWindow.clearMessages();
				finalOrderWindow.hide();
			} catch (BackendException e) {
				finalOrderWindow.displayError("Backend Exception: "+e.getMessage());
			}catch(RuleException e){
				finalOrderWindow.displayError("Error: "+e.getMessage());
			}catch(BusinessException e){
				finalOrderWindow.displayError("Business Error: "+e.getMessage());
			}

		}

	}

	public SubmitHandler getSubmitHandler() {
		return new SubmitHandler();
	}

	private class CancelOrderHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow.displayInfo("Order Cancelled");
		}
	}

	public CancelOrderHandler getCancelOrderHandler() {
		return new CancelOrderHandler();
	}

	private class ToShoppingCartFromFinalOrderHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			finalOrderWindow.hide();
			finalOrderWindow.clearMessages();
		}
	}

	public ToShoppingCartFromFinalOrderHandler getToShoppingCartFromFinalOrderHandler() {
		return new ToShoppingCartFromFinalOrderHandler();
	}

	// handlers for OrderCompleteWindow

	private class ContinueFromOrderCompleteHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			CatalogListWindow.getInstance().show();
			orderCompleteWindow.hide();
		}
	}

	public ContinueFromOrderCompleteHandler getContinueFromOrderCompleteHandler() {
		return new ContinueFromOrderCompleteHandler();
	}

	public void deleteAddress(ObservableList<CustomerPres> selectedItems){

	}
}
