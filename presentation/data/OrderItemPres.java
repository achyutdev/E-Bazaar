package presentation.data;

import javafx.beans.property.SimpleStringProperty;
import business.externalinterfaces.OrderItem;
import business.ordersubsystem.OrderItemImpl;

public class OrderItemPres {
	public OrderItemPres(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	public OrderItemPres() {}

	private OrderItem orderItem;

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public SimpleStringProperty itemIdProperty() {
		return new SimpleStringProperty(
				(new Integer(orderItem.getOrderItemId())).toString());
	}

	public SimpleStringProperty orderIdProperty() {
		return new SimpleStringProperty(
				(new Integer(orderItem.getOrderId())).toString());
	}
	public SimpleStringProperty quantityProperty() {
		return new SimpleStringProperty(
				(new Integer(orderItem.getQuantity())).toString());
	}
	public SimpleStringProperty unitPriceProperty() {
		return new SimpleStringProperty(
				(new Double(orderItem.getUnitPrice())).toString());
	}

	public SimpleStringProperty totalPriceProperty() {
		return new SimpleStringProperty(
				String.format("%.2f",(new Double(orderItem.getTotalPrice()))));
	}


	public SimpleStringProperty productNameProperty() {
		return new SimpleStringProperty(orderItem.getProductName());
	}

	public void setOrderId(SimpleStringProperty orderId) {
		orderItem.setOrderId(Integer.parseInt(orderId.get()));
	}
	public void setItemId(SimpleStringProperty itemId) {
		orderItem.setOrderItemId(Integer.parseInt(itemId.get()));
	}
	public void setProductName(SimpleStringProperty prodName) {
		orderItem.setProductName(prodName.get());
	}
	public void setQuantity(SimpleStringProperty q) {
		orderItem.setQuantity(Integer.parseInt(q.get()));
	}
	public void setUnitPrice(SimpleStringProperty price) {
		orderItem.setUnitPrice(Double.parseDouble(price.get()));
	}
	/*
	 * private int itemID; private int orderID; private final String
	 * productName; private final int quantity; private final double unitPrice;
	 */

}
