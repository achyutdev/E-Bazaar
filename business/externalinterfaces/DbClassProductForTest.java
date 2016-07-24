package business.externalinterfaces;

import java.util.List;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

/* Used only for testing DbClassAddress */
public interface DbClassProductForTest extends DbClass {
	public void saveNewProduct(Product product, Catalog catalog) throws DatabaseException;
}
