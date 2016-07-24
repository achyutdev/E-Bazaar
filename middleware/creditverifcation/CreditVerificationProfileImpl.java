package middleware.creditverifcation;

import middleware.externalinterfaces.CreditVerificationProfile;

public class CreditVerificationProfileImpl implements CreditVerificationProfile {
	private String firstName;
	private String lastName;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String cardNum;
	private String expirationDate;
	private double amount;
	
	CreditVerificationProfileImpl() {}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCardNum() {
		return cardNum;
	}
public String getExpirationDate() {
	return expirationDate;
}
	public double getAmount() {
		return amount;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public void setExpirationDate(String date) {
		expirationDate = date;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
