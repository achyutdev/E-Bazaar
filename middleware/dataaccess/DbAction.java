
package middleware.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;


/**
 * This is the Mediator class for the Data Access Subsystem.
 * It is responsible for executing one query from a DbClass instance,
 * using one Connection.
 * @author pcorazza
 *
 */
class DbAction {
	private static final Logger LOG
	    = Logger.getLogger(DbAction.class.getName());
    protected String query;
    protected PreparedStatement stmt;
    protected Object[] params;
    protected int[] paramTypes;
    protected DbClass concreteDbClass;
    protected Connection con;
    protected SimpleConnectionPool pool = SimpleConnectionPool.INSTANCE;

    DbAction(DbClass c) throws DatabaseException {
        concreteDbClass = c;
        con = pool.getConnection(concreteDbClass.getDbUrl());
    }
    /**
     * This method extracts query, params, types from the concreteDbClass
     * that was set when connection was established.
     * It must be called as the first step of each method that
     * executes a query (and only then)
     */
    private void extractQueryData() {
    	//System.out.println("DbAction:extractQueryData:....");
    	query = concreteDbClass.getQuery();
    	params = concreteDbClass.getQueryParams();
    	paramTypes = concreteDbClass.getParamTypes();
    }

    void performRead() throws DatabaseException {
    	//System.out.println("DbAction:performRead:....");
    	extractQueryData();
    	stmt = StatementPrep.createGeneralPreparedStatement(
    			con, query, params, paramTypes);
    	try {
	    	ResultSet rs = stmt.executeQuery();
			concreteDbClass.populateEntity(rs);
    	} catch(SQLException e) {
    		throw new DatabaseException(e);
    	}
    }

    Integer performInsert() throws DatabaseException {
    	extractQueryData();
    	stmt = StatementPrep.createInsertPreparedStatement(
    			con, query, params, paramTypes);
    	LOG.info(stmt.toString());
    	try {
    		stmt.executeUpdate();
    		int key = -1;
    		ResultSet rs = stmt.getGeneratedKeys();
    		if (rs.next()) {
    			key = rs.getInt(1);
    		} else {
    			LOG.info("No return value for " + stmt.toString());
    		}
    		return key;
    	} catch(SQLException e) {
    		throw new DatabaseException(e);
    	}
    }

    Integer performUpdateOrDelete() throws DatabaseException {
    	extractQueryData();
    	stmt = StatementPrep.createInsertPreparedStatement(con, query, params, paramTypes);
    	LOG.info(stmt.toString());
    	try {
    		int result = stmt.executeUpdate();
    		return result;
    	} catch(SQLException e) {
    		throw new DatabaseException(e);
    	}
    }

    void returnToPool() throws DatabaseException {
    	pool.returnToPool(con, concreteDbClass.getDbUrl());
    }

    void startTransaction() throws DatabaseException {
    	try {
			con.setAutoCommit(false);
		} catch(SQLException e) {
			throw new DatabaseException("DbAction.startTransaction() " +
				"encountered a SQLException " + e.getMessage());
		}
    }
    void commit() throws DatabaseException {
    	LOG.info("Performing COMMIT");
    	try {
			con.commit();
		} catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
    }
    public void rollback() throws DatabaseException {
		try {
			con.rollback();
		} catch(SQLException e) {
			throw new DatabaseException("rollback encountered a SQLException " + e.getMessage());
		}
	}

}
