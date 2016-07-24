package presentation.control;

import business.exceptions.UnauthorizedException;
import business.usecasecontrol.BrowseAndSelectController;
import business.util.DataUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.data.*;
import presentation.gui.OrderDetailWindow;
import presentation.gui.OrdersWindow;
import presentation.gui.ShoppingCartWindow;

public enum ViewOrdersUIControl {
	INSTANCE;

	private OrdersWindow ordersWindow;
	private OrderDetailWindow orderDetailWindow;
    private Stage primaryStage;
    private Callback startScreenCallback;

	public void setPrimaryStage(Stage ps, Callback returnMessage) {
		primaryStage = ps;
		startScreenCallback = returnMessage;
	}

	private class ViewOrdersHandler implements EventHandler<ActionEvent>,Callback {
		public void doUpdate() {
			try {
	    		Authorization.checkAuthorization(ordersWindow, DataUtil.custIsAdmin());
	    	} catch(UnauthorizedException e) {
	    		displayError(e.getMessage());
	        	return;
	        }
			ordersWindow = new OrdersWindow(primaryStage);
			ordersWindow.setData(FXCollections.observableList(ViewOrdersData.INSTANCE.getOrders()));
			ordersWindow.show();
	        primaryStage.hide();
		}
		@Override
		public Text getMessageBar() {
			return startScreenCallback.getMessageBar();
		}
		@Override
		public void handle(ActionEvent evt) {
			boolean isLoggedIn = DataUtil.isLoggedIn();
			if (!isLoggedIn) {
				LoginUIControl loginControl = new LoginUIControl(ordersWindow,
						primaryStage, this);
				loginControl.startLogin();
			} else {
				doUpdate();
			}
		}

	}
	public ViewOrdersHandler getViewOrdersHandler() {
		return new ViewOrdersHandler();
	}

	//OrdersWindow
	private class ViewOrderDetailsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			TableView<OrderPres> table = ordersWindow.getTable();
			OrderPres selectedOrder = table.getSelectionModel().getSelectedItem();
			if(selectedOrder == null) {
				ordersWindow.displayError("Please select a row.");
			} else {
				ordersWindow.clearMessages();
				orderDetailWindow = new OrderDetailWindow();
				orderDetailWindow.setData(FXCollections.observableList(selectedOrder.getOrderItemsPres()));
				ordersWindow.hide();
				orderDetailWindow.show();
			}
		}
	}

	public ViewOrderDetailsHandler getViewOrderDetailsHandler() {
		return new ViewOrderDetailsHandler();
	}


	//OrderDetailWindow
	private class OrderDetailsOkHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ordersWindow.show();
			orderDetailWindow.hide();
		}
	}
	public OrderDetailsOkHandler getOrderDetailsOkHandler() {
		return new OrderDetailsOkHandler();
	}

	private class CancelHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			ordersWindow.hide();
			startScreenCallback.clearMessages();
			primaryStage.show();

		}
	}
	public CancelHandler getCancelHandler() {
		return new CancelHandler();
	}
}
