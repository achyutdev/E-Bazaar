package business.productsubsystem;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;
import presentation.data.ManageProductsData;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.util.TwoKeyHashMap;

public class ProductSubsystemFacade implements ProductSubsystem {
	private static final Logger LOG = 
			Logger.getLogger(ProductSubsystemFacade.class.getPackage().getName());
	public static Catalog createCatalog(int id, String name) {
		return new CatalogImpl(id, name);
	}
	public static Product createProduct(Catalog c, String name, 
			LocalDate date, int numAvail, double price) {
		return new ProductImpl(c, name, date, numAvail, price);
	}
	public static Product createProduct(Catalog c, Integer pi, String pn, int qa, 
			double up, LocalDate md, String desc) {
		return new ProductImpl(c, pi, pn, qa, up, md, desc);
	}
	
	/** obtains product for a given product name */
    public Product getProductFromName(String prodName) throws BackendException {
    	try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(getProductIdFromName(prodName));
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}	
    }
    public Integer getProductIdFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			TwoKeyHashMap<Integer,String,Product> table = dbclass.readProductTable();
			return table.getFirstKey(prodName);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
    public Product getProductFromId(Integer prodId) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(prodId);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
	}
    
    public List<Catalog> getCatalogList() throws BackendException {
    	try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes().getCatalogs();
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
		
    }
    
    public List<Product> getProductList(Catalog catalog) throws BackendException {
    	try {
    		DbClassProduct dbclass = new DbClassProduct();
    		return dbclass.readProductList(catalog);
    	} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
    }
	
	public int readQuantityAvailable(Product product) {
		return product.getQuantityAvail();
	}
	
	public int saveNewCatalog(String catalogName) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.saveNewCatalog(catalogName);
		} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
	}
	
	@Override
	public void saveNewProduct(Product product) throws BackendException {
		DbClassProduct dbprod=new DbClassProduct();
		try {
			dbprod.saveNewProduct(product, product.getCatalog());
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
	@Override
	public void deleteProduct(Product product) throws BackendException {
		DbClassProduct dbProd= new DbClassProduct();
		try {
			dbProd.deleteProduct(product.getProductId());
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
	@Override
	public void deleteCatalog(Catalog catalog) throws BackendException {
		DbClassCatalog dbCat=new DbClassCatalog();
		try {
			dbCat.deleteCatalog(catalog.getId());
			
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
	
	/**update catalog*/
	@Override
	public void updateCatalog(Catalog catalog) throws BackendException{
		DbClassCatalog cat=new DbClassCatalog();
		try {
			cat.updateCatalog(catalog);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}
	
	/**udpate product*/
	@Override
	public void updateProduct(Product prod) throws BackendException {
		DbClassProduct dbprod=new DbClassProduct();
		try {
			dbprod.updateProduct(prod);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		
	}
	
	public DbClassProductForTest getGenericDbClassProductImpl(){
		return new  DbClassProduct();
	}
	
}
