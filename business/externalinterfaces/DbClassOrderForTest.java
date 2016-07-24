package business.externalinterfaces;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassOrderForTest extends DbClass{
   public  void submitOrder(CustomerProfile custProfile, Order order) throws DatabaseException ;
}
