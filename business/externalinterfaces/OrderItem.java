
package business.externalinterfaces;


public interface OrderItem {
    public int getOrderItemId();
    public int getProductId();
    public int getOrderId();
    public int getQuantity();
    public double getUnitPrice();
    public double getTotalPrice();
    public String getProductName();
    public void setProductName(String name);
    public void setOrderId(int orderid);
    public void setOrderItemId(int id);
    public void setQuantity(int id);
    public void setUnitPrice(double u);
    public void setProductId(int id);
    public double getShippingCost();
    public double getTaxAmount();

}
