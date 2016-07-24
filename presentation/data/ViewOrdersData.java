package presentation.data;

import java.util.List;

import java.util.logging.Logger;

import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.usecasecontrol.ViewOrdersController;
import business.util.DataUtil;
import presentation.util.UtilForUIClasses;

public enum ViewOrdersData {
	INSTANCE;
	private static final Logger LOG =
		Logger.getLogger(ViewOrdersData.class.getSimpleName());
	private OrderPres selectedOrder;
	public OrderPres getSelectedOrder() {
		return selectedOrder;
	}
	public void setSelectedOrder(OrderPres so) {
		selectedOrder = so;
	}

	public List<OrderPres> getOrders() {
		CustomerSubsystem cust= DataUtil.readCustFromCache();
		List<Order> orderHistory= ViewOrdersController.INSTANCE.getOrderHistory(cust);
		return UtilForUIClasses.orderListToOrderPresList(orderHistory);
	}
}
