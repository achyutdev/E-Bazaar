package business.externalinterfaces;

import java.util.List;


public interface ShoppingCart {
    Address getShippingAddress();
    Address getBillingAddress();
    CreditCard getPaymentInfo();
    List<CartItem> getCartItems();
    void setCartItems(List<CartItem> cartItems);
    double getTotalPrice();
    boolean deleteCartItem(String name);
    boolean isEmpty();
    void setShipAddress(Address addr);
    void setBillAddress(Address addr);

    void setPaymentInfo(CreditCard cc);

    //setters for testing
    
}
