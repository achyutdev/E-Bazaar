
package middleware.externalinterfaces;

import middleware.exceptions.DatabaseException;

public interface DataAccessSubsystem {
	//This starts the data access interaction and permits the use of transactions
	//The DataAccessSubsystem will keep a reference to the connection for all future uses in the current instance
	//so the connection object does not need to be managed by the client.
	public void establishConnection(DbClass dbClass) throws DatabaseException;
	public void releaseConnection() throws DatabaseException;
	public void startTransaction() throws DatabaseException;
	public void commit() throws DatabaseException;
	public void rollback() throws DatabaseException;
    public void read() throws DatabaseException;  
    public Integer insert() throws DatabaseException;
    public Integer update() throws DatabaseException;
    public Integer delete() throws DatabaseException;
	public Integer insertWithinTransaction(DbClass dbClass) throws DatabaseException;
	public Integer updateWithinTransaction(DbClass dbClass) throws DatabaseException;
	public Integer deleteWithinTransaction(DbClass dbClass) throws DatabaseException;
	public void atomicRead(DbClass dbClass) throws DatabaseException;
	
		
	
}
