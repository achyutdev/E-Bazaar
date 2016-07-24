
package business.usecasecontrol;

import java.util.List;

import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;

/**
 * @author pcorazza
 */
public enum ViewOrdersController   {
	INSTANCE;
	
	public List<Order> getOrderHistory(CustomerSubsystem cust) {
		return cust.getOrderHistory();
	}
	
	
}
