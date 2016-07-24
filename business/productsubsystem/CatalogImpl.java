package business.productsubsystem;

import business.externalinterfaces.Catalog;


class CatalogImpl implements Catalog {
	private int id;
	private String name;
	public CatalogImpl() {
		//do nothing
	}
	CatalogImpl(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String n) {
		name = n;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean equals(Object ob) {
		if(ob == null) return false;
		if(this == ob) return true;
		if(getClass() != ob.getClass()) return false;
		CatalogImpl c = (CatalogImpl)ob;
		return name.equals(c.name);
	}
	
	public int hashCode() {
		int result = 17;
		result += 31 * result + name.hashCode();
		return result;
	}
	
}
