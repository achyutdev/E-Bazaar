
package business.externalinterfaces;

public interface Address {
    public String getStreet();
    public String getCity();
    public String getState();
    public String getZip();
    public void setStreet(String s);
    public void setCity(String s);
    public void setState(String s);
    public void setZip(String s);
    public boolean isShippingAddress();
	public boolean isBillingAddress();
	public void setShippingAddress(boolean isShippingAddress);
	public void setBillingAddress(boolean isBillingAddress);
}
