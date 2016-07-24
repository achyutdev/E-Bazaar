package business.util;


import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import business.BusinessConstants;
import business.SessionCache;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CustomerSubsystem;

public class DataUtil {
	
	public static boolean custIsAdmin() {
		CustomerSubsystem cust = readCustFromCache();
        return (cust != null && cust.isAdmin());
	}
	
	public static boolean isLoggedIn() {
		return (Boolean)SessionCache.getInstance().get(BusinessConstants.LOGGED_IN);
	}
	
	public static CustomerSubsystem readCustFromCache() {
		return (CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
	}
	public static double computeTotal(List<CartItem> list) {
		DoubleSummaryStatistics summary 
		  = list.stream().collect(
		       Collectors.summarizingDouble(item -> Double.parseDouble(item.getTotalprice())));
		return summary.getSum();
	}
}
