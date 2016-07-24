package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.DbClassProductForTest;
import business.externalinterfaces.Product;
import business.util.Convert;
import business.util.TwoKeyHashMap;

class DbClassProduct implements DbClassProductForTest {
	enum Type {LOAD_PROD_TABLE, READ_PRODUCT, READ_PROD_LIST, SAVE_NEW_PROD,DELETE_PRODUCT,UPDATE_PRODUCT};

	private static final Logger LOG = Logger.getLogger(DbClassProduct.class
			.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	private Type queryType;

	private String loadProdTableQuery = "SELECT * FROM product";
	private String readProductQuery = "SELECT * FROM Product WHERE productid = ?";
	private String readProdListQuery = "SELECT * FROM Product WHERE catalogid = ?";
	private String saveNewProdQuery = "INSERT INTO Product"
			+ "(catalogid,productname,totalquantity,priceperunit,mfgdate,description) "
			+ "values(?,?,?,?,?,?)"; 
	private String deleteProductQuery="DELETE FROM Product WHERE productid=?";
	private String updateProductQuery="UPDATE Product SET "
			+ "productname=?,"
			+ "totalQuantity=?,"
			+ "priceperunit=?,"
			+ "mfgdate=?,"
			+ "description=?"
			+ "Where productid=?";
	
	private Object[] loadProdTableParams, readProductParams, 
		readProdListParams, saveNewProdParams,deleteProdParams,updateProdParams;
	private int[] loadProdTableTypes, readProductTypes, readProdListTypes, 
	    saveNewProdTypes,deleteProdTypes,updateProdTypes;
	
	/**
	 * The productTable matches product ID and product name with
	 * the corresponding Product object. It is static so
	 * that requests for "read product" based on product ID can be handled
	 * without extra db hits. Useful for customer use cases, but not
	 * for manage products use case
	 */
	private static TwoKeyHashMap<Integer, String, Product> productTable;
	private Product product;
	private List<Product> productList;

	public TwoKeyHashMap<Integer, String, Product> readProductTable()
			throws DatabaseException {
		if (productTable != null) {
			return productTable.clone();
		}
		//productTable needs to be populated, so call refresh
		return refreshProductTable();
	}

	/**
	 * Force a database call
	 */
	public TwoKeyHashMap<Integer, String, Product> refreshProductTable()
			throws DatabaseException {
		queryType = Type.LOAD_PROD_TABLE;
		loadProdTableParams = new Object[]{};
		loadProdTableTypes = new int[]{};
		dataAccessSS.atomicRead(this);
		
		// Return a clone since productTable must not be corrupted
		return productTable.clone();
	}

	public List<Product> readProductList(Catalog cat)
			throws DatabaseException {
		if (productList == null) {
			return refreshProductList(cat);
		}
		return Collections.unmodifiableList(productList);
	}

	public List<Product> refreshProductList(Catalog cat)
			throws DatabaseException {
		queryType = Type.READ_PROD_LIST;
		readProdListParams = new Object[]{cat.getId()};
		readProdListTypes = new int[]{Types.INTEGER};
		dataAccessSS.atomicRead(this);
		return productList;
	}

	public Product readProduct(Integer productId)
			throws DatabaseException {
		if (productTable != null && productTable.isAFirstKey(productId)) {
			return productTable.getValWithFirstKey(productId);
		}
		queryType = Type.READ_PRODUCT;
		readProductParams = new Object[] {productId};
		readProductTypes = new int[] {Types.INTEGER};
		dataAccessSS.atomicRead(this);
		return product;
	}
	

	/**
	 * Database columns: productid, productname, totalquantity, priceperunit,
	 * mfgdate, catalogid, description
	 */
	public void saveNewProduct(Product product, Catalog catalog) 
			throws DatabaseException {
		//implement
		//LOG.warning("Method saveNewProduct in DbClassProduct has not been impemented");
		queryType=Type.SAVE_NEW_PROD;
		saveNewProdParams = new Object[] {
				catalog.getId(),
				product.getProductName(),
				product.getQuantityAvail(),
				product.getUnitPrice(),
				Convert.localDateAsString(product.getMfgDate()),
				product.getDescription()
		};
		saveNewProdTypes = new int[] {
				Types.INTEGER,
				Types.VARCHAR,
				Types.INTEGER,
				Types.DOUBLE,
				Types.VARCHAR,
				Types.VARCHAR };
		dataAccessSS.insertWithinTransaction(this);
		
	}
	
	/// DbClass implemented methods
	@Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

	@Override
	public String getQuery() {
		switch(queryType) {
			case LOAD_PROD_TABLE:
				return loadProdTableQuery;
			case READ_PRODUCT:
				return readProductQuery;
			case READ_PROD_LIST:
				return readProdListQuery;
			case SAVE_NEW_PROD :
				return saveNewProdQuery;
			case DELETE_PRODUCT:
				return deleteProductQuery;
			case UPDATE_PRODUCT:
				return updateProductQuery;
			default:
				return null;
		}
	}
	
	@Override
	public Object[] getQueryParams() {
		switch(queryType) {
			case LOAD_PROD_TABLE:
				return loadProdTableParams;
			case READ_PRODUCT:
				return readProductParams;
			case READ_PROD_LIST:
				return readProdListParams;
			case SAVE_NEW_PROD :
				return saveNewProdParams;
			case DELETE_PRODUCT:
				return deleteProdParams;
			case UPDATE_PRODUCT:
				return updateProdParams;
			default:
				return null;
		}
	}
	
	@Override
	public int[] getParamTypes() {
		switch(queryType) {
		case LOAD_PROD_TABLE:
			return loadProdTableTypes;
		case READ_PRODUCT:
			return readProductTypes;
		case READ_PROD_LIST:
			return readProdListTypes;
		case SAVE_NEW_PROD :
			return saveNewProdTypes;
		case DELETE_PRODUCT:
			return deleteProdTypes;
		case UPDATE_PRODUCT:
			return updateProdTypes;
		default:
			return null;
	}
	}

	@Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		switch(queryType) {
			case LOAD_PROD_TABLE :
				populateProdTable(resultSet);
			case READ_PRODUCT :
				populateProduct(resultSet);
			case READ_PROD_LIST :
				populateProdList(resultSet);
			default :
				//do nothing
		}
	}

	private void populateProdList(ResultSet rs) throws DatabaseException {
		productList = new LinkedList<Product>();
		try {
			Product product = null;
			Integer prodId = null;
			String productName = null;
			Integer quantityAvail = null;
			Double unitPrice = null;
			String mfgDate = null;
			Integer catalogId = null;
			String description = null;
			while (rs.next()) {
				prodId = rs.getInt("productid");
				productName = rs.getString("productname");
				quantityAvail = rs.getInt("totalquantity");
				unitPrice =rs.getDouble("priceperunit");
				mfgDate = rs.getString("mfgdate");
				catalogId = rs.getInt("catalogid");
				description = rs.getString("description");
				CatalogImpl catalog = new CatalogImpl(catalogId, 
						(new CatalogTypesImpl()).getCatalogName(catalogId));
				product = new ProductImpl(catalog, prodId, productName, quantityAvail,
						 unitPrice, Convert.localDateForString(mfgDate),
					    description);
				productList.add(product);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * Internal method to ensure that product table is up to date.
	 */
	private void populateProdTable(ResultSet rs) throws DatabaseException {
		productTable = new TwoKeyHashMap<Integer, String, Product>();
		try {
			Product product = null;
			Integer prodId = null;
			String productName = null;
			Integer quantityAvail = null;
			Double unitPrice = null;
			String mfgDate = null;
			Integer catalogId = null;
			String description = null;
			while (rs.next()) {
				prodId = rs.getInt("productid");
				productName = rs.getString("productname");
				quantityAvail = rs.getInt("totalquantity");
				unitPrice = rs.getDouble("priceperunit");
				mfgDate = rs.getString("mfgdate");
				catalogId = rs.getInt("catalogid");
				description = rs.getString("description");
				CatalogImpl catalog = new CatalogImpl(catalogId, 
						(new CatalogTypesImpl()).getCatalogName(catalogId));
				product = new ProductImpl(catalog, prodId, productName, quantityAvail,
						unitPrice, Convert.localDateForString(mfgDate), description);
				productTable.put(prodId, productName, product);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	private void populateProduct(ResultSet rs) throws DatabaseException {
		try {
			if (rs.next()) {
				CatalogImpl catalog = new CatalogImpl(rs.getInt("catalogid"), 
						(new CatalogTypesImpl()).getCatalogName(rs.getInt("catalogid")));
				
				product = new ProductImpl(catalog, rs.getInt("productid"),
						rs.getString("productname"),
						rs.getInt("totalquantity"),
						rs.getDouble("priceperunit"),
						Convert.localDateForString(rs.getString("mfgdate")),
						rs.getString("description"));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	
	public void deleteProduct(int productid) throws DatabaseException{
		queryType=Type.DELETE_PRODUCT;
		deleteProdParams=new Object[]{productid};
		deleteProdTypes=new int[]{Types.INTEGER};
		dataAccessSS.deleteWithinTransaction(this);
		
	}
	
	public void updateProduct(Product product) throws DatabaseException {
		queryType=Type.UPDATE_PRODUCT;

		updateProdParams=new Object[]{
				product.getProductName(),
				product.getQuantityAvail(),
				product.getUnitPrice(),
				Convert.localDateAsString(product.getMfgDate()),
				product.getDescription(),
				product.getProductId()
		};
		
		updateProdTypes=new int[]{
				Types.VARCHAR, 
				Types.INTEGER,
				Types.DOUBLE,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.INTEGER
		};
		dataAccessSS.updateWithinTransaction(this);
	}
}
