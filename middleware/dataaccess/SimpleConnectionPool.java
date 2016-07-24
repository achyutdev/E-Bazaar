package middleware.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbConfigKey;


/**
 *  This is a singleton. It requests a value for max num of connections, but
 *  does not enforce this limit.
 */
enum SimpleConnectionPool {
	INSTANCE;
	SimpleConnectionPool() {}
	
	
	static DbConfigProperties props = new DbConfigProperties();
	// maps URI -> free connections queue
	private HashMap<String, Queue<Connection>> freeConnectionsMap = new HashMap<String, Queue<Connection>>();

	// maps URI -> numclients (Integer)
	private static HashMap<String, Integer> numClientsMap = new HashMap<String, Integer>();
	private static final Logger LOG = Logger.getLogger(SimpleConnectionPool.class
			.getPackage().getName());
	private static final String DEFAULT_DRIVER = props.getProperty(DbConfigKey.DRIVER
			.getVal());
	private static final String DEFAULT_USER = props.getProperty(DbConfigKey.DB_USER
			.getVal());
	private static final String DEFAULT_MAX_CONN = props
			.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal());

	private String dbuser;

	private String dbpass;

	private String drivername;

	private int maxconn;
	
	///////// INITIALIZE INSTANCE //////////////
	
	synchronized void init(int maxconn) throws DatabaseException {
		init("", "", "", maxconn);
	}
    /** Called only once during execution, by DataAccessUtil.initializePool() 
     */   
	synchronized void init(String dbuser, String dbpass, String drivername, int maxconn)
			throws DatabaseException {
		this.dbuser = (dbuser==null || dbuser.isEmpty()) ? DEFAULT_USER : dbuser;
		this.dbpass = dbpass;
		this.drivername = (drivername==null || drivername.isEmpty()) ? DEFAULT_DRIVER
				: drivername;
		int biggest = Integer.parseInt(DEFAULT_MAX_CONN);
		this.maxconn = (maxconn <=0 || maxconn > biggest) ? biggest : maxconn;
		if(this.drivername != null) loadJDBCDriver();
	}
	
	private void loadJDBCDriver() throws DatabaseException {
		try {
			Class.forName(drivername);
		} catch (java.lang.ClassNotFoundException e) {
			LOG.warning("ClassNotFoundException: " + e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	/////////// INITIALIZE CONNECTIONS MAP & GET CONNECTION ///////////////

	synchronized Connection getConnection(String URI) throws DatabaseException {
		if (noClientsSoFar(URI)) {
			initializePool(URI);
		}
		Connection con = null;
		Queue<Connection> freeConnections = getListOfFreeConnections(URI);
		if (freeConnections != null && !freeConnections.isEmpty()) {
			LOG.info("Returning a live connection from the pool");
			con = freeConnections.poll();
			try {
				if (con.isClosed()) {
					LOG.info("Removed closed connection!");
					// Try again recursively
					con = getConnection(URI);
				}
			} catch (Exception e) {
				LOG.info("Exception occurred in checking whether connection is closed");
				// Try again recursively
				con = getConnection(URI);
			}
		} else {  //nothing available in the queue; create new connection
			con = createConnection(URI);
		}
		//one way or another, we got a new connection; 
		//doing this may cause num connections to exceed maxconn
		incrementNumClients(URI);
		return con;
	}
	
	private Connection createConnection(String URI) throws DatabaseException {
		Connection con = null;
		try {
			if (this.dbuser == null || dbuser.equals("")) {
				con = DriverManager.getConnection(URI);
			} else {
				con = DriverManager
						.getConnection(URI, this.dbuser, this.dbpass);
			}
			// add connection to pool
			LOG.info("Adding new connection to pool");
			addConnection(URI, con);

		} catch (SQLException e) {
			LOG.warning("Unable to create a connection to database with dburl "
					+ URI);
			throw new DatabaseException("Database is unavailable.");
		}
		return con;
	}
	private boolean noClientsSoFar(String URI) {
		Integer numCl = numClientsMap.get(URI);
		return (numCl == null || numCl.intValue() == 0);
	}
	private void initializePool(String URI) throws DatabaseException {
		for (int i = 0; i < maxconn; ++i) {
			createConnection(URI);

		}
	}
	private void addConnection(String URI, Connection con) {
		Queue<Connection> freeForURI = freeConnectionsMap.get(URI);
		if (freeForURI == null) {
			freeForURI = new LinkedList<Connection>();
		}

		freeForURI.add(con);
		freeConnectionsMap.put(URI, freeForURI);
	}
	private  void incrementNumClients(String URI) {
		Integer num = numClientsMap.get(URI);
		if (num == null) {
			numClientsMap.put(URI, new Integer(1));

		} else {
			Integer next = new Integer(num.intValue() + 1);
			numClientsMap.put(URI, next);
		}
	}
	
	///////////// RETURNING CONN TO POOL /////////////////

	synchronized void returnToPool(Connection con, String URI) {
		try {
			if ((con != null) && (!con.isClosed())
					&& (numFreeConnections(URI) < this.maxconn)) {
				LOG.info("Returning a connection to pool");
				con.setAutoCommit(true);
				addConnection(URI, con);
			}
		} catch (Exception e) {
			LOG.warning("Unable to return connection to pool. Proceeding..."
					+ e.getMessage());
			// do nothing -- just don't return con to the pool
		}
	}
	
	private Queue<Connection> getListOfFreeConnections(String URI) {
		return freeConnectionsMap.get(URI);
	}

	private int numFreeConnections(String URI) {
		Queue<Connection> cons = getListOfFreeConnections(URI);
		if (cons == null)
			return 0;
		return cons.size();

	}

	
	///////////////// CLOSING CONNECTIONS ////////////////
	
	synchronized void closeConnections() {
		LOG.info("Closing all connections");
		Collection<Queue<Connection>> conLists = freeConnectionsMap.values();
		for (Queue<Connection> list : conLists) {
			releaseConnectionsInQueue(list);
		}
	}

	private synchronized void releaseConnectionsInQueue(
			Queue<Connection> freeConnections) {
		if (freeConnections == null)
			return;
		for (Connection con : freeConnections) {
			try {
				con.close();
			} catch (SQLException e) {
				LOG.warning("Cannot close connection! (Probably already closed?)");
			}
		}
		freeConnections.clear();
	}

	//////////// FOR TESTING ////////////
	
	

}
