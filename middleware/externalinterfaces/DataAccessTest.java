package middleware.externalinterfaces;

import java.sql.ResultSet;

import middleware.exceptions.DatabaseException;

public interface DataAccessTest {
	public ResultSet[] multipleInstanceQueries(String[] queries, String[] dburls) throws DatabaseException;
}
