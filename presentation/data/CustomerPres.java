package presentation.data;

import business.externalinterfaces.*;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is used to present customer name and address
 * information in tableviews. There is no corresponding
 * entity class; instead it is a combination of
 * Address and CustomerProfile entities.
 *
 */
public class CustomerPres {
	private final SimpleStringProperty firstName = new SimpleStringProperty();
	private final SimpleStringProperty lastName = new SimpleStringProperty();
	private CustomerProfile custProfile;
	private Address address;
	public CustomerPres(CustomerProfile custProfile, Address addr) {
		this.custProfile = custProfile;
		address = addr;
	}
	public CustomerPres(CustomerProfile custProfile) {
		this(custProfile, null);
		System.out.println("m claled in customerpres once++................/////////..........."+address);
	}
	public CustomerPres() {
		this(null, null);
	}
	public SimpleStringProperty firstNameProperty() {
		return new SimpleStringProperty(custProfile.getFirstName());
	}
	public SimpleStringProperty lastNameProperty() {
		return new SimpleStringProperty(custProfile.getLastName());
	}
	public SimpleStringProperty fullNameProperty() {
		return new SimpleStringProperty(custProfile.getFirstName() + " " + custProfile.getLastName());
	}
	public CustomerProfile getCustProfile() {
		return custProfile;
	}
	public void setCustProfile(CustomerProfile custProfile) {
		this.custProfile = custProfile;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address a) {
		System.out.println("adress set at customerpres:"+a.getCity());
		address = a;
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
	public void setStreet(SimpleStringProperty s) {
		address.setStreet(s.get());
	}
	public void setCity(SimpleStringProperty s) {
		address.setCity(s.get());
	}
	public void setState(SimpleStringProperty s) {
		address.setState(s.get());
	}
	public void setZip(SimpleStringProperty s) {
		address.setZip(s.get());
	}

}
