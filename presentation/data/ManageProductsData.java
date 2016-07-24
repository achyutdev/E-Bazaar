package presentation.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.util.Convert;
import business.productsubsystem.ProductSubsystemFacade;

public enum ManageProductsData {
	INSTANCE;
	private ObservableList<CatalogPres> catalogList = readCatalogsFromDataSource();
	private CatalogPres defaultCatalog = readDefaultCatalogFromDataSource();
	
	
	/** Initializes the catalogList 
	 * @throws BackendException */
	private ObservableList<CatalogPres> readCatalogsFromDataSource()  {
		ProductSubsystemFacade pss=new ProductSubsystemFacade();
		
		List<Catalog> catalogs=new ArrayList<>();
		try {
			catalogs = pss.getCatalogList();
		} catch (BackendException e) {
			 throw new ExceptionInInitializerError(e);
		}
		List<CatalogPres> pres=catalogs.stream()
								.map(x->convertCatalogtoCatalogPres(x))
								.collect(Collectors.toList());
		ObservableList<CatalogPres> observable=FXCollections.observableList(pres);
		return observable;
	}
	private CatalogPres readDefaultCatalogFromDataSource() {
			return catalogList.get(0);
	}
	public CatalogPres getDefaultCatalog() {
		return defaultCatalog;
	}
	
	private CatalogPres selectedCatalog = defaultCatalog;
	public void setSelectedCatalog(CatalogPres selCatalog) {
		selectedCatalog = selCatalog;
	}
	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}
	//////////// Products List model
	private ObservableMap<CatalogPres, List<ProductPres>> productsMap
	   = readProductsFromDataSource();
	
	/** Initializes the productsMap 
	 * @throws BackendException */
	private ObservableMap<CatalogPres, List<ProductPres>> readProductsFromDataSource()  {
		ObservableMap<CatalogPres, List<ProductPres>> catMap=FXCollections.observableHashMap();
		ProductSubsystemFacade pss=new ProductSubsystemFacade();
		for(CatalogPres cat:catalogList){
			try {
			List<ProductPres> prodlist;
				prodlist = pss.getProductList(cat.getCatalog())
						.stream().map(x->productPresFromProduct(x)).collect(Collectors.toList());
				catMap.put(cat, prodlist);
			} catch (BackendException e) {
				e.printStackTrace();
			}
			
		}
		return catMap;
	}
	
	/** Delivers the requested products list to the UI */
	public ObservableList<ProductPres> getProductsList(CatalogPres catPres) {
		return FXCollections.observableList(productsMap.get(catPres));
	}
	
	public ProductPres productPresFromData(Catalog c, String name, String date,  //MM/dd/yyyy 
			int numAvail, double price) {
		
		Product product = ProductSubsystemFacade.createProduct(c, name, 
				Convert.localDateForString(date), numAvail, price);
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(product);
		return prodPres;
	}
	
	public void addToProdList(CatalogPres catPres, ProductPres prodPres) {
		ObservableList<ProductPres> newProducts =
		           FXCollections.observableArrayList(prodPres);
		List<ProductPres> specifiedProds = productsMap.get(catPres);
		
		//Place the new item at the bottom of the list
		Boolean result=specifiedProds.addAll(newProducts);
		if(result){
			productsMap.put(catPres, FXCollections.observableList(specifiedProds));
		}
		
	}
	
	/** This method looks for the 0th element of the toBeRemoved list 
	 *  and if found, removes it. In this app, removing more than one product at a time
	 *  is  not supported.
	 * @throws BackendException 
	 */
	public boolean removeFromProductList(CatalogPres cat, ObservableList<ProductPres> toBeRemoved) throws BackendException {
		if(toBeRemoved != null && !toBeRemoved.isEmpty()) {
			boolean result =false;
			ProductSubsystemFacade pss=new ProductSubsystemFacade();
			try{
				pss.deleteProduct(toBeRemoved.get(0).getProduct());
				productsMap.get(cat).remove(toBeRemoved.get(0));
				result=true;
			}
			catch(BackendException e) {
				throw new BackendException(e);
			}
			
			return result;
		}
		return false;
	}
		
	//////// Catalogs List model
	

	

	/** Delivers the already-populated catalogList to the UI */
	public ObservableList<CatalogPres> getCatalogList() {
		return catalogList;
	}

	public CatalogPres catalogPresFromData(int id, String name) {
		Catalog cat = ProductSubsystemFacade.createCatalog(id, name);
		CatalogPres catPres = new CatalogPres();
		catPres.setCatalog(cat);
		return catPres;
	}

	public void addToCatalogList(CatalogPres catPres) {
		ObservableList<CatalogPres> newCatalogs = FXCollections
				.observableArrayList(catPres);

		// Place the new item at the bottom of the list
		// catalogList is guaranteed to be non-null
		boolean result = catalogList.addAll(newCatalogs);
		if(result) { //must make this catalog accessible in productsMap
			productsMap.put(catPres, FXCollections.observableList(new ArrayList<ProductPres>()));
		}
	}

	/**
	 * This method looks for the 0th element of the toBeRemoved list in
	 * catalogList and if found, removes it. In this app, removing more than one
	 * catalog at a time is not supported.
	 * 
	 * This method also updates the productList by removing the products that
	 * belong to the Catalog that is being removed.
	 * 
	 * Also: If the removed catalog was being stored as the selectedCatalog,
	 * the next item in the catalog list is set as "selected"
	 * @throws BackendException 
	 */
	public boolean removeFromCatalogList(ObservableList<CatalogPres> toBeRemoved) throws BackendException {
		boolean result = false;
		CatalogPres item = toBeRemoved.get(0);
		ProductSubsystemFacade pss=new ProductSubsystemFacade();
		if (toBeRemoved != null && !toBeRemoved.isEmpty()) {
			pss.deleteCatalog(item.getCatalog());
			catalogList.remove(item);
			result=true;
		}
		if(item.equals(selectedCatalog)) {
			if(!catalogList.isEmpty()) {
				selectedCatalog = catalogList.get(0);
			} else {
				selectedCatalog = null;
			}
		}
		if(result) {//update productsMap
			productsMap.remove(item);
		}
		return result;
	}
	
	//Synchronizers
	public class ManageProductsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			productsMap.put(selectedCatalog, list);
		}
	}
	public ManageProductsSynchronizer getManageProductsSynchronizer() {
		return new ManageProductsSynchronizer();
	}
	
	private class ManageCatalogsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			catalogList = list;
		}
	}
	public ManageCatalogsSynchronizer getManageCatalogsSynchronizer() {
		return new ManageCatalogsSynchronizer();
	}
	
	//convert catalog to catalogPres
	public CatalogPres convertCatalogtoCatalogPres(Catalog catalog){
		CatalogPres catPres=new CatalogPres();
		catPres.setCatalog(catalog);
		return catPres;
	}
	
	public ProductPres productPresFromProduct(Product product){
		ProductPres prodPres=new ProductPres();
		prodPres.setProduct(product);
		return prodPres;
	}
	
	
	public ProductPres productPresFromDataWithDesc(Catalog c, String name, String date,  //MM/dd/yyyy 
			int numAvail, double price,String desc) {
		Product product = ProductSubsystemFacade.createProduct(c,0, name, numAvail,price,
				Convert.localDateForString(date),  desc);
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(product);
		return prodPres;
	}
	
}
