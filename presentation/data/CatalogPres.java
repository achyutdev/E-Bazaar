package presentation.data;

import business.externalinterfaces.Catalog;
import javafx.beans.property.SimpleStringProperty;

public class CatalogPres {
	private Catalog catalog;
    public CatalogPres() {}
    
    public void setCatalog(Catalog catalog) {
    	this.catalog = catalog;
    }
    public Catalog getCatalog() {
    	return catalog;
    }
    
    public SimpleStringProperty nameProperty() {
    	return new SimpleStringProperty(catalog.getName());	
    }
    public void setName(SimpleStringProperty aName) {
    	catalog.setName(aName.get());
    }
    
    public SimpleStringProperty idProperty() {
    	return new SimpleStringProperty((new Integer(catalog.getId())).toString());
    }
    
    public void setId(SimpleStringProperty id) {
    	catalog.setId(Integer.parseInt(id.get()));
    }
    
    public boolean equals(Object ob) {
		if(ob == null) return false;
		if(this == ob) return true;
		if(getClass() != ob.getClass()) return false;
		CatalogPres c = (CatalogPres)ob;
		return catalog.equals(c.catalog);
	}
	
	public int hashCode() {
		int result = 17;
		result += 31 * result + catalog.hashCode();
		return result;
	}
}
