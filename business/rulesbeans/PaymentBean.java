package business.rulesbeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import business.externalinterfaces.*;

public class PaymentBean implements DynamicBean {
	private CreditCard cc;
	private Address addr;
	public PaymentBean(Address addr, CreditCard cc) {
		this.addr = addr;
		this.cc = cc;
	}
	//////////// bean interface for address and credit card
	public String getCity() {
        return addr.getCity();
    }
 
    public String getState() {
        return addr.getState();
    }
 
     public String getStreet() {
        return addr.getStreet();
    }
 
    public String getZip() {
        return addr.getZip();
    }
    public String getCardNum(){
    	
    	return cc.getCardNum();
    }
    public String getExpirationDate(){
    	return cc.getExpirationDate();
    }
    public String getNameOnCard() {
    	return cc.getNameOnCard();
    }
    
    public String getCardType() {
    	return cc.getCardType();
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
