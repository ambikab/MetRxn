package org.code.metrxn.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.code.metrxn.util.DBUtil;
import org.code.metrxn.util.SearchCriteria;

/**
 * 
 * @author ambika_b
 *
 */

public class QueryRepository {

	//TODO: connection pool to be configured.
	Connection connection;

	public QueryRepository() {
		connection = DBUtil.getConnection();
	}

	public int getTotalCount(SearchCriteria searchCriteria) throws SQLException {
		String queryString = "select count(1) from (" + searchCriteria.getSearchString() + ") alias";
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(queryString);
		int count = -1;
		while (rs.next()) 
			count = rs.getInt(1);
		return count;
	}

	public List<HashMap<String, Object>> fetchResults(SearchCriteria searchCriteria) throws SQLException {
		int tableLoad = searchCriteria.getNumberOfRecords();
		int offset = ((searchCriteria.getReqPageNo() - 1) * tableLoad);
		String queryString = searchCriteria.getSearchString() 
				+ " order by "	+ searchCriteria.getSortColumn() + " " + searchCriteria.getSortOrder() 
				+ " limit " + offset + " , " + tableLoad;
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(queryString);
		ResultSetMetaData metaData = rs.getMetaData();

		int columnCount = metaData.getColumnCount();
		List<HashMap<String, Object>> resultSet = new ArrayList<HashMap<String, Object>>();
		
		while (rs.next()) {
			HashMap<String, Object> row = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) { 
				row.put(metaData.getColumnLabel(i), rs.getObject(i));
			}
			resultSet.add(row);
		}
		return resultSet;
	}
}
