
package middleware.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DataAccessTest;
import middleware.externalinterfaces.DbClass;

/**
 * Facade for access to the Data Access Subsystem. Instances of this class
 * dispatch all requests for creating and releasing a connection
 * and for reading and writing data to the objects within the
 * Data Access Subsystem, which work directly with JDBC.
 *
 * @author pcorazza
 *
 */
public class DataAccessSubsystemFacade implements DataAccessSubsystem, DataAccessTest {
	//connection pool is initialized just once, at startup
	static {
		try {
			DataAccessUtil.initializePool();
		} catch(DatabaseException e) {
			//if initialization fails
			throw new RuntimeException("Data Access initialization failed: " + e.getMessage());
		}
	}
	public DataAccessSubsystemFacade() {
    	//do nothing
    }

	private static final Logger LOG
	  = Logger.getLogger(DataAccessSubsystemFacade.class.getPackage().getName());

	DbAction action;

	/**
	 * Gets a connection from the SimpleConnectionPool and stores
	 * it in a DbAction, which is cached here. Note that all
	 * methods for data requests in DataAccess require
	 * a call to establishConnection as the first step.
	 * In insertWithInTransaction, deleteWithinTransaction,
	 * and updateWithinTransaction, and also in the atomicRead method,
	 * the methods establishConnection and releaseConnection are
	 * called explicitly. On the other hand, the calls to
	 * read, save and delete are "raw" -- they need to be preceded
	 * by a call to establishConenction and followed by a call
	 * to releaseConnection. Typically, these raw methods are parts
	 * of a user-created transaction in which establishConnection
	 * is called at the beginning and releaseConnection called at the
	 * end. A template for performing transactions can be found
	 * in the implementation of updateWithinTransaction.
	 */
	public void establishConnection(DbClass dbClass) throws DatabaseException {
		if(dbClass != null) {
			action = new DbAction(dbClass);
		}
		else {
			throw new DatabaseException("Cannot establish connection - DbClass is null");
		}
	}

	/**
	 * Returns connection to pool and sets autoCommit to true.
	 */
	public void releaseConnection() throws DatabaseException {
		action.returnToPool();
	}

	/**
	 * Sets autoCommit to false. (Note: autocommit is set back to true when
	 * connection is returned to pool.)
	 *
	 * Precondition: A Connection has been obtained via establishConnection
	 */
	public void startTransaction() throws DatabaseException {
		action.startTransaction();
	}

	/**
	 * Precondition: A Connection has been obtained via establishConnection
	 */
	public void commit() throws DatabaseException {
		action.commit();
	}

	/**
	 * Precondition: A Connection has been obtained via establishConnection
	 */
	public void rollback() throws DatabaseException {
		action.rollback();
	}

	///// Raw read, delete, update methods -- typically used as part of a bigger transaction //////

	/**
	 * Precondition: A Connection has been obtained via establishConnection.
	 * User must manually releaseConnection after read has completed.
	 * Can use atomicRead to handle createConnection and releaseConnection
	 * if no other data access code is bundled with the read.
	 */
    public void read() throws DatabaseException {
    	try	{
    		action.performRead();
    	}
    	catch(Exception e){
    		System.out.println("Error:"+e.getMessage());
    	}


    }

    /**
	 * Precondition: A Connection has been obtained via establishConnection. User
	 * of this code must manually releaseConnection after operation has completed.
	 * Returns generated key, if there is one.
	 */
    public Integer insert() throws DatabaseException  {
        return action.performInsert();
    }

    /**
 	 * Precondition: A Connection has been obtained via establishConnection. User
	 * of this code must manually releaseConnection after operation has completed.
     * Returns number of rows affected
     */
    public Integer update() throws DatabaseException  {
    	return action.performUpdateOrDelete();
    }

    /**
     * Precondition: A Connection has been obtained via establishConnection. User
	 * of this code must manually releaseConnection after operation has completed.
	 * Returns number of rows deleted
     */
    public Integer delete() throws DatabaseException  {
       return action.performUpdateOrDelete();
    }


    ////// Convenience methods for data access operations that are already atomic ////////

	/**
	 * This convenience method carries out a typical insert/update within a transaction. To wrap
	 * multiple or complex sql operations in a transaction, use startTransaction instead. Note
	 * that establishConnection is called as part of the method body (so a separate call to
	 * establishConnection is not required in this case). Likewise, releaseConnection is handled
	 * automatically.
	 */
	public Integer insertWithinTransaction(DbClass dbClass)
			throws DatabaseException {
		establishConnection(dbClass);
		startTransaction();
        try {
        	int autoGenKey = insert();
        	commit();
        	return autoGenKey;
        } catch(DatabaseException e) {
        	LOG.warning("Attempting to rollback...");
        	rollback();
        	throw (e);
        }  finally {
        	releaseConnection();
        }
	}

	/**
	 * This convenience method carries out a typical insert/update within a transaction. To wrap
	 * multiple or complex sql operations in a transaction, use startTransaction instead. Note
	 * that establishConnection is called as part of the method body (so a separate call to
	 * establishConnection is not required in this case). Likewise, releaseConnection is handled
	 * automatically.
	 */
	public Integer updateWithinTransaction(DbClass dbClass)
			throws DatabaseException {
		establishConnection(dbClass);
		startTransaction();
        try {
        	int numRows = update();
        	commit();
        	return numRows;
        } catch(DatabaseException e) {
        	LOG.warning("Attempting to rollback...");
        	rollback();
        	throw (e);
        }  finally {
        	releaseConnection();
        }
	}

	/**
	 * This convenience method carries out a typical delete within a transaction. To wrap
	 * multiple or complex sql operations in a transaction, use startTransaction instead. Note
	 * that establishConnection is called as part of the method body (so a separate call to
	 * establishConnection is not required in this case). Likewise, releaseConnection is handled
	 * automatically.
	 */
	public Integer deleteWithinTransaction(DbClass dbClass)
			throws DatabaseException {
		establishConnection(dbClass);
		startTransaction();
		int numRows = 0;
        try {
        	numRows = delete();
        	commit();
        	return numRows;
        } catch(DatabaseException e) {
        	LOG.warning("Attempting to rollback...");
        	rollback();
        	throw (e);
        }  finally {
        	releaseConnection();
        }
	}

	/**
	 * This convenience method performs a single read operation. It handles
	 * the establishConnection and releaseConnection steps.
	 */
	public void atomicRead(DbClass dbClass)
			throws DatabaseException {
		establishConnection(dbClass);
		read();
		releaseConnection();
	}

	/**
	 * Literally closes every connection in the connection pool.
	 */
	public void closeAllConnections(){
        SimpleConnectionPool.INSTANCE.closeConnections();
	}


    //Testing interface
    public ResultSet[] multipleInstanceQueries(String[] queries, String[] dburls) throws DatabaseException {
    	if(queries == null || dburls == null) return null;
    	if(queries.length != dburls.length) return null;
    	int numConnections = queries.length;
    	ResultSet[] results = new ResultSet[numConnections];
        ArrayList<Connection> cons = new ArrayList<Connection>();
        for(int i = 0; i < numConnections; ++i) {
        	cons.add(SimpleConnectionPool.INSTANCE.getConnection(dburls[i]));
        }
        //need to rethink this
//        for(int i = 0; i < numConnections; ++i) {
//        	results[i] = SimpleConnectionPool.doQuery(cons.get(i), queries[i]);
//        }
        return results;

    }

}
