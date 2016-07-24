package business.productsubsystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import business.externalinterfaces.*;
import javafx.beans.property.*;

public class ProductImpl implements Product {
	private String productName;
    private int productId;     
	private int quantityAvail; 
    private Catalog catalog;
    private double unitPrice;
    private LocalDate mfgDate;
    private String description;
    private int quantityRequested;
    //convert all non-string types to strings if used in a table; otherwise, don't convert
    public int getQuantityRequested() {
		return quantityRequested;
	}
	public void setQuantityRequested(int quantityRequested) {
		this.quantityRequested=quantityRequested;
	}
	public ProductImpl(Catalog c, Integer pi, String pn, int qa, 
			double up, LocalDate md, String d){
        if(pi != null) productId = pi;
        this.productName=pn;
        quantityAvail=qa;
        unitPrice=up;
        mfgDate=md;
        catalog = c;
        Optional.ofNullable(d).ifPresent(x -> description = x);
    }
    //this constructor is used when getting user-entered data in adding a new product
    public ProductImpl(Catalog c, String name, LocalDate date, int numAvail, double price){
    	this(c, null, name, numAvail, price, date, null);
    }
    
    
    public Catalog getCatalog() {
        return catalog;
    }
    
    public LocalDate getMfgDate() {    	
    	return mfgDate;
    }
    
    public int getProductId() {
        return productId;
    }
    /**
     * @return Returns the productName.
     */
    public String getProductName() {
        return productName;
    }
    /**
     * @return Returns the quantityAvail.
     */
    public int getQuantityAvail() {
       return quantityAvail;
    }
    /**
     * @return Returns the unitPrice.
     */
    public double getUnitPrice() {
       return unitPrice;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
	public void setDescription(String description) {
		this.description = description;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setQuantityAvail(int quantityAvail) {
		this.quantityAvail = quantityAvail;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public void setMfgDate(LocalDate mfgDate) {
		this.mfgDate = mfgDate;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}

}
