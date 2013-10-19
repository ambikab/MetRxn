package org.code.metrxn.repository.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.code.metrxn.enums.TableMap;
import org.code.metrxn.model.uploader.FileColumn;
import org.code.metrxn.model.uploader.ColumnMapping;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.util.DBUtil;

/**
 * 
 * @author ambika_b
 *
 */
public class MapperRepository {

	Connection connection;

	public MapperRepository() {
		connection = DBUtil.getConnection();
	}

	/**
	 * fetches the mapping between the file columns and the database columns
	 * @param uId
	 * @param entityName
	 * @param contents
	 * @return
	 */
	public TableMapper fetchDbMappings(String uId, String entityType,  Map<String, FileColumn> contents) {
		String tableName = Enum.valueOf(TableMap.class, entityType).tableValue();
		System.out.println("Chosen table is :" + tableName);
		PreparedStatement preparedStatement;
		Map<String, ArrayList<ColumnMapping>> mapper = new HashMap<String, ArrayList<ColumnMapping>>();
		try {
			String fetchCols = "select * from " + tableName + " limit 1";
			preparedStatement = connection.prepareStatement(fetchCols);
			ResultSet rs = preparedStatement.executeQuery(fetchCols);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount=rsmd.getColumnCount();
			System.out.println("number of columns in the table is " + colCount);
			String batchIns = "insert into entity_mapping values(?,?,?,?)";
			PreparedStatement batchStatement = connection.prepareStatement(batchIns);
			for (int i = 1; i <= colCount; i++) { 
				// contains all the mappings for a given table column with all the columns in the file
				ArrayList<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>(); 
				for (java.util.Iterator<String> keys = contents.keySet().iterator(); keys.hasNext();) { //fileColumns
					String fileCol = keys.next();
					String findMap = "select count(1) from " + tableName + " where " + rsmd.getColumnName(i) + " in ( " + contents.get(fileCol).getData() + ")";
					PreparedStatement psCount = connection.prepareStatement(findMap);
					ResultSet rsCnt = psCount.executeQuery(findMap);
					rsCnt.next();
					columnMappings.add(new ColumnMapping(fileCol, rsCnt.getInt(1)));
					batchStatement.setString(1, tableName);
					batchStatement.setString(2, uId);
					batchStatement.setString(3, fileCol);
					batchStatement.setString(1, rsmd.getColumnName(i));
					batchStatement.addBatch();
				}
				mapper.put(rsmd.getColumnName(i), columnMappings);
			}
			batchStatement.executeBatch();
		} catch (SQLException e) {
			System.out.println("Issue in fetching the image for database\n");
		}
		return new TableMapper(entityType,uId, mapper);// return boolean
	}

}
