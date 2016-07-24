package business.rulesbeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.ShoppingCart;

public class ShopCartBean implements DynamicBean {
	//private static Logger log = Logger.getLogger(null);
    
	private ShoppingCart shopCart;
	public ShopCartBean(ShoppingCart sc){		
		shopCart = sc;
	}
	
	
	///////bean interface for shopping cart
	public boolean getIsEmpty() {
		return shopCart.isEmpty();
	}
	public Address getShippingAddress() {
		return shopCart.getShippingAddress();
	}
    public Address getBillingAddress(){
		return shopCart.getBillingAddress();
	}
    public CreditCard getPaymentInfo(){
		return shopCart.getPaymentInfo();
	}
    public List<CartItem> getCartItems(){
		return shopCart.getCartItems();
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
