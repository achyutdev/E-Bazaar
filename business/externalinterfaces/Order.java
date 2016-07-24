
package business.externalinterfaces;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;


public interface Order {
    public List<OrderItem> getOrderItems();
	public LocalDate getOrderDate();
	public int getOrderId();
	public double getTotalPrice();
	public double getTotalTax();
	public double getTotalShippingCharge();
	public double getTotalAmountCharged();
    public void setOrderItems(List<OrderItem> orderItems);
	public void setOrderId(int orderId);
	public void setShippAdress(Address sa);
	public void setBillAddress(Address ba);
	public void setPaymentCard(CreditCard cc);

	public Address getShipAddress();
    public Address getBillAddress();
    public CreditCard getPaymentInfo();

	//implement

//	public void setDate(LocalDate date);
//	public void setShipAddress(Address add);
//	public void setBillAddress(Address add);
//	public void setPaymentInfo(CreditCard cc);


}








