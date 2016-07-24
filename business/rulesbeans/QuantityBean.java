package business.rulesbeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import business.externalinterfaces.DynamicBean;


public class QuantityBean implements DynamicBean {
	//Represent as a String instead of int so we can test validity of input
	private String quantityRequested;
	private int quantityAvailable;
	public QuantityBean(String quantityRequested, int quantityAvail) {
		this.quantityRequested= quantityRequested;
		this.quantityAvailable = quantityAvail;
	}
	
	//////////// bean interface for address
	public int getQuantityAvailable() {
		return quantityAvailable;
	}
 
    public String getQuantityRequested(){
        return quantityRequested;
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
