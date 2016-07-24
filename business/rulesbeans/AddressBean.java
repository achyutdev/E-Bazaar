package business.rulesbeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import business.externalinterfaces.*;

public class AddressBean implements DynamicBean {
	private Address address;
	public AddressBean(Address addr) {
		address = addr;
	}
	public String getCity() {
		return address.getCity();
	}
	public void setCity(String city) {
		address.setCity(city);
	}
	public String getState() {
		return address.getState();
	}
	public void setState(String state) {
		address.setState(state);
	}
	public String getStreet() {
		return address.getStreet();
	}
	public void setStreet(String street) {
		address.setStreet(street);
	}
	public String getZip() {
		return address.getZip();
	}
	public void setZip(String zip) {
		address.setZip(zip);
	}
	///////////property change listener code
    private PropertyChangeSupport pcs = 
    	new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener pcl){
	 	pcs.addPropertyChangeListener(pcl);
	}
	public void removePropertyChangeListener(PropertyChangeListener pcl){	
    	pcs.removePropertyChangeListener(pcl);
    }

}
