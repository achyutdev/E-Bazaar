package presentation.data;

import business.externalinterfaces.*;
import javafx.beans.property.SimpleStringProperty;

public class AddressPres {
	private Address address;
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public SimpleStringProperty streetProperty() {
		return new SimpleStringProperty(address.getStreet());
	}
	public SimpleStringProperty cityProperty() {
		return new SimpleStringProperty(address.getCity());
	}
	public SimpleStringProperty stateProperty() {
		return new SimpleStringProperty(address.getState());
	}
	public SimpleStringProperty zipProperty() {
		return new SimpleStringProperty(address.getZip());
	}
	public void setStreet(SimpleStringProperty aStreet) {
		address.setStreet(aStreet.get());
	}
	public void setCity(SimpleStringProperty aCity) {
		address.setCity(aCity.get());
	}
	public void setState(SimpleStringProperty aState) {
		address.setState(aState.get());
	}
	public void setZip(SimpleStringProperty aZip) {
		address.setZip(aZip.get());
	}
	public SimpleStringProperty shippingAddressProperty() {
		return new SimpleStringProperty((new Boolean(address.isShippingAddress()).toString()));
	}
	
	public SimpleStringProperty billingAddressProperty() {
		return new SimpleStringProperty((new Boolean(address.isBillingAddress()).toString()));
	}
	
}
