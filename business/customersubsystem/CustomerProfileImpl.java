package business.customersubsystem;

import business.externalinterfaces.CustomerProfile;

class CustomerProfileImpl implements CustomerProfile{
	private String firstName;
	private String lastName;
	private Integer custId;
	private boolean isAdmin;
	//CustomerProfile(){}
	CustomerProfileImpl(Integer custid, String firstName, String lastName, boolean isAdmin) {
		this.custId = custid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isAdmin = isAdmin;
	}
	CustomerProfileImpl(Integer custid, String fName, String lName) {
		this(custid, fName, lName, false);
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer id) {
		custId = id;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean b) {
		isAdmin = b;
	}
}
