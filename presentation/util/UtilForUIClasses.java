package presentation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import presentation.data.*;
import business.externalinterfaces.*;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;

public class UtilForUIClasses {
	private final static String REL_RULES_PATH = "business/rulefiles";
	public static BufferedReader pathToRules(ClassLoader classLoader, String filename) throws IOException {
	    URL url = classLoader.getResource(REL_RULES_PATH + "/" + filename);
	    BufferedReader buf = new BufferedReader((new InputStreamReader(url.openStream())));
	    return buf;
	}
	
	public static CatalogPres catalogToCatalogPres(Catalog catalog) {
		CatalogPres retVal = new CatalogPres();
		retVal.setCatalog(catalog);
		return retVal;
	}
	
	public static ProductPres productToProductPres(Product product) {
		ProductPres retVal = new ProductPres();
		retVal.setProduct(product);
		return retVal;
	}
	
	public static List<CartItem> cartItemPresToCartItemList(List<CartItemPres> list) {
		if(list == null) return null;
		return list.stream()
				.map(pres -> pres.getCartItem())
				.map(cartdata -> ShoppingCartSubsystemFacade.createCartItem(cartdata.getItemName(), 
						String.valueOf(cartdata.getQuantity()), String.valueOf(cartdata.getTotalPrice())))
				.collect(Collectors.toList());
		
	}
	
	public static List<CartItemPres> cartItemsToCartItemPres(List<CartItem> list) {
		if(list == null) return null;
		return list.stream()
				.map(c -> {CartItemData d = new CartItemData(); 
				    d.setItemName(c.getProductName()); 
				    double total = Double.parseDouble(c.getTotalprice());
				    int quantity = Integer.parseInt(c.getQuantity()); 
				    d.setPrice(total/quantity);
				    d.setQuantity(quantity); return d;})
				.map(c -> {CartItemPres p = new CartItemPres(); p.setCartItem(c); return p;})
				.collect(Collectors.toList());
		
	}
	
	public static List<OrderPres> orderListToOrderPresList(List<Order> list) {
		if(list == null) return null;
		return list.stream()
				.map(ord -> {OrderPres op = new OrderPres(); op.setOrder(ord); return op;})
				.collect(Collectors.toList());
		
	}
}
