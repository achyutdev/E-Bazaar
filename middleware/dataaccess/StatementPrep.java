package middleware.dataaccess;

import java.sql.*;
import middleware.exceptions.DatabaseException;

/**
 * Responsible for creating and populating a PreparedStatement
 * using as input:
 * - a query (with 0 or more unspecified parameters),
 * - an array of parameter values
 * - an array of types for those values, specified using
 *   the constants in the class java.sql.Types.
 */
public class StatementPrep {

	public static PreparedStatement createInsertPreparedStatement(Connection con, String query, Object[] params,
			int[] paramTypes) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			setValues(stmt, params, paramTypes);
			return stmt;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public static PreparedStatement createGeneralPreparedStatement(Connection con, String query, Object[] params,
			int[] paramTypes) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(query);
			setValues(stmt, params, paramTypes);
			return stmt;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}

	}
	private static void setValues(PreparedStatement stmt, Object[] params, int[] paramTypes) throws SQLException {
		int count = 0;
		for (int i = 0; i < paramTypes.length; ++i) {
			++count;
			int type = paramTypes[i];
			switch (type) {
			case Types.BIGINT:
				stmt.setInt(count, (Integer) params[i]);
				break;
			case Types.BOOLEAN:
				stmt.setBoolean(count, (Boolean) params[i]);
				break;
			case Types.DATE:
				stmt.setDate(count, (java.sql.Date) params[i]);
				break;
			case Types.DOUBLE:
				stmt.setDouble(count, (Double) params[i]);
				break;
			case Types.FLOAT:
				stmt.setDouble(count, (Float) params[i]);
				break;
			case Types.INTEGER:

				stmt.setInt(count, (Integer) params[i]);
				break;
			case Types.NULL:
				stmt.setNull(count, 0);
				break;
			case Types.VARCHAR:
				stmt.setString(count, (String) params[i]);
				break;
			default:
				stmt.setObject(count, params[i]);
			}
		}
	}
}
