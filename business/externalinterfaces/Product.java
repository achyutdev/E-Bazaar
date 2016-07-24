
package business.externalinterfaces;
import java.time.LocalDate;

public interface Product {
	
    public LocalDate getMfgDate();
    
    public int getProductId();
    
    public String getProductName();
    public Catalog getCatalog();
    public int getQuantityAvail();
    public double getUnitPrice();
    public String getDescription();
	public void setDescription(String description);
	public void setProductName(String productName);
	public void setQuantityAvail(int quantityAvail);
	public void setUnitPrice(double unitPrice);
	public void setMfgDate(LocalDate mfgDate);
	public void setProductId(int productId);
}
