
package business.usecasecontrol;

import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

import com.mysql.jdbc.log.Log;

import middleware.exceptions.DatabaseException;
import presentation.data.CatalogPres;
import presentation.data.ManageProductsData;
import presentation.data.ProductPres;
import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductImpl;
import business.productsubsystem.ProductSubsystemFacade;
import javafx.collections.ObservableList;


public enum ManageProductsController   {
    INSTANCE;
    private static final Logger LOG = 
    	Logger.getLogger(ManageProductsController.class.getName());
    private ProductSubsystem pss = new ProductSubsystemFacade();
    
    /**
     * returns a list of products*/
    public List<Product> getProductsList(CatalogPres catalog) throws BackendException {
    	return pss.getProductList(catalog.getCatalog());  
    }
    
    /**returns a list of catalogs*/
    public List<Catalog> getCatalogsList() throws BackendException {
    	return pss.getCatalogList();
    }
    
    
    public int saveNewCatalog(String catName) throws BackendException {
    	return pss.saveNewCatalog(catName);
    }
    
    
    public void deleteProduct(CatalogPres cat,ObservableList<ProductPres> selectedItems) throws BackendException {
    	ManageProductsData.INSTANCE.removeFromProductList(cat,selectedItems);
    }
    
    //save new product
    public void saveProduct(Product prod) throws BackendException{
    	//ProductSubsystem pss = new ProductSubsystemFacade(); 
    	pss.saveNewProduct(prod);
    	
    }
    
    public void deleteCatalog(ObservableList<CatalogPres> toBeRemoved) throws BackendException {
    	ManageProductsData.INSTANCE.removeFromCatalogList(toBeRemoved);
    }
    
   
    public void updateCatalog(CatalogPres catalog) throws BackendException{
    	try{
    	pss.updateCatalog(catalog.getCatalog());
    	} catch (BackendException e) {
			LOG.warning(e.getMessage());
			throw new BackendException(e);
		}
    }
    
    public void updateProduct(ProductPres product) throws BackendException {
    	try {
			pss.updateProduct(product.getProduct());
		} catch (BackendException e) {
			LOG.warning(e.getMessage());
			throw new BackendException(e);
		}
    }
    
    
}
