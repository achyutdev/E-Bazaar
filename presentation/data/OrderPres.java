package presentation.data;

import java.time.format.DateTimeFormatter;

import presentation.gui.GuiConstants;
import presentation.util.GuiUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import business.externalinterfaces.Order;
import business.util.Convert;

public class OrderPres {
	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	public SimpleStringProperty orderIdProperty() {
		return new SimpleStringProperty(String.valueOf(order.getOrderId()));
	}
	public SimpleStringProperty dateProperty() {
		System.out.println("order date"+ order.getOrderDate());
		return new  SimpleStringProperty(
			Convert.localDateAsString(order.getOrderDate()));

	}
	public SimpleStringProperty totalPriceProperty() {
		return new SimpleStringProperty(
				String.format("%.2f", order.getTotalPrice()));
	}

	public ObservableList<OrderItemPres> getOrderItemsPres() {

		return GuiUtils.orderItemsToOrderItemsPres(order.getOrderItems());
	}

}
