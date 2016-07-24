package presentation.control;

import java.util.HashMap;
import java.util.Map;

import business.exceptions.UnauthorizedException;
import javafx.stage.Stage;
import presentation.data.ErrorMessages;
import presentation.gui.MaintainCatalogsWindow;
import presentation.gui.MaintainProductsWindow;
import presentation.gui.OrdersWindow;
import presentation.gui.ShippingBillingWindow;
import presentation.gui.ShoppingCartWindow;

public class Authorization {
	
	public static Map<Class<? extends Stage>, Boolean> acl 
	   = new HashMap<Class<? extends Stage>, Boolean>();
    
    static {
    	//acl map tells you whether a user aiming at window (key) requires
    	//admin authorization to arrive there 
    	acl.put(ShippingBillingWindow.class, Boolean.FALSE);
    	acl.put(ShoppingCartWindow.class, Boolean.FALSE);
    	acl.put(OrdersWindow.class, Boolean.FALSE);
    	acl.put(MaintainCatalogsWindow.class, Boolean.TRUE);
    	acl.put(MaintainProductsWindow.class, Boolean.TRUE);
    	
    }
    
    private static boolean requiresAdmin(Stage c) {
    	if(c == null) return false;
    	Class<? extends Stage> input = c.getClass();
    	if(!acl.containsKey(input)) return false;
    	return acl.get(input);
    }
    
    public static void checkAuthorization(Stage c, boolean custIsAdmin) 
    		throws UnauthorizedException {
    	boolean requiresAdmin = requiresAdmin(c);
    	if(requiresAdmin && !custIsAdmin) {
    		throw new UnauthorizedException(ErrorMessages.NOT_AUTHORIZED);
    	}

    }
    
    
}
