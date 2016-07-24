package middleware.externalinterfaces;

public interface CreditVerificationProfile {
	public String getFirstName();

	public String getLastName();

	public String getStreet();

	public String getCity();

	public String getState();

	public String getZip();

	public String getCardNum();
	public String getExpirationDate();

	public double getAmount();

	public void setFirstName(String firstName);

	public void setLastName(String lastName);

	public void setStreet(String street);

	public void setCity(String city);

	public void setState(String state);

	public void setZip(String zip);
	public void setCardNum(String cardNum);
public void setExpirationDate(String date);
	public void setAmount(double amount);
}
