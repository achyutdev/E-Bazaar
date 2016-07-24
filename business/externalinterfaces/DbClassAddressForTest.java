package business.externalinterfaces;

import java.util.List;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

/* Used only for testing DbClassAddress */
public interface DbClassAddressForTest extends DbClass {
	public List<Address> readAllAddresses(CustomerProfile custProfile) throws DatabaseException;
}
