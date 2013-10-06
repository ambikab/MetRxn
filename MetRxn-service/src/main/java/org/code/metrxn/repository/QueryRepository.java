package org.code.metrxn.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.code.metrxn.util.DBUtil;
import org.code.metrxn.util.SearchCriteria;

/**
 * Executes the given query string on the database and returns the results.
 * @author ambika_b
 *
 */
public class QueryRepository {

	//TODO: connection pool to be configured.
	Connection connection;

	/**
	 * fetches the connection from the connection pool 
	 * and uses this connection for the database operations.
	 */
	public QueryRepository() {
		connection = DBUtil.getConnection();
	}

	/**
	 * Fetches the count of all the results that matched the given query.
	 * @param searchCriteria
	 * @return
	 * @throws SQLException
	 */
	public int getTotalCount(SearchCriteria searchCriteria) throws SQLException {
		String queryString = "select count(1) from (" + searchCriteria.getSearchString() + ") alias";
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(queryString);
		int count = -1;
		while (rs.next()) 
			count = rs.getInt(1);
		return count;
	}

	/**
	 * Fetches the sorted result set on execution of the query. 
	 * The results are fetched only for the given page number.
	 * Pagination is applied by calculating the offset and the limit.
	 * The results are sorted based on the name of the chosen column name.
	 * @param searchCriteria
	 * @return
	 * @throws SQLException
	 */
	public List<LinkedHashMap<String, Object>> fetchResults(SearchCriteria searchCriteria) throws SQLException {
		int tableLoad = searchCriteria.getNumberOfRecords();
		int offset = ((searchCriteria.getReqPageNo() - 1) * tableLoad);
		String queryString = searchCriteria.getSearchString() 
				+ " order by "	+ searchCriteria.getSortColumn() + " " + searchCriteria.getSortOrder() 
				+ " limit " + offset + " , " + tableLoad;
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(queryString);
		ResultSetMetaData metaData = rs.getMetaData();

		int columnCount = metaData.getColumnCount();
		List<LinkedHashMap<String, Object>> resultSet = new ArrayList<LinkedHashMap<String, Object>>();
		
		while (rs.next()) {
			LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) { 
				row.put(metaData.getColumnLabel(i), rs.getObject(i));
			}
			resultSet.add(row);
		}
		return resultSet;
	}
}
