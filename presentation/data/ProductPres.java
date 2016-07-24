package presentation.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import business.externalinterfaces.*;
import business.util.Convert;

public class ProductPres {
	private Product product;
    public ProductPres() {}
    
    public void setProduct(Product product) {
    	this.product = product;
    }
    public Product getProduct() {
    	return product;
    }
    
    public SimpleStringProperty nameProperty() {
    	return new SimpleStringProperty(product.getProductName());	
    }
    public void setName(SimpleStringProperty aName) {
    	product.setProductName(aName.get());
    }
    
    public SimpleStringProperty idProperty() {
    	return new SimpleStringProperty((new Integer(product.getProductId())).toString());
    }
    public void setId(SimpleStringProperty idStr) {
    	product.setProductId(Integer.parseInt(idStr.get()));
    }
    public SimpleStringProperty mfgDateProperty() {
    	return new SimpleStringProperty(
    		product.getMfgDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
    public void setMfgDate(SimpleStringProperty date) {
    	product.setMfgDate(Convert.localDateForString(date.get()));
    }
    public SimpleStringProperty descriptionProperty() {
    	return new SimpleStringProperty(product.getDescription());
    }
    public void setDescription(SimpleStringProperty d) {
    	product.setDescription(d.get());
    }
    public SimpleStringProperty unitPriceProperty() {
    	return new SimpleStringProperty(String.format("%.2f",product.getUnitPrice()));
    }
    public void setUnitPrice(SimpleStringProperty up) {
    	 product.setUnitPrice(Double.parseDouble(up.get()));
    }
    public SimpleStringProperty quantityAvailProperty() {
    	return new SimpleStringProperty((new Integer(product.getQuantityAvail())).toString());
    }
    public void setQuantityAvail(SimpleStringProperty qa) {
    	 product.setQuantityAvail(Integer.parseInt(qa.get()));
    }
}
