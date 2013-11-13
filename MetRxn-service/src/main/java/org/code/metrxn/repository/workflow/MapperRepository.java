package org.code.metrxn.repository.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.code.metrxn.enums.TableMap;
import org.code.metrxn.model.uploader.FileColumn;
import org.code.metrxn.model.uploader.ColumnMapping;
import org.code.metrxn.model.uploader.FileData;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.util.DBUtil;
import org.code.metrxn.util.Logger;

/**
 * 
 * @author ambika_b
 *
 */
public class MapperRepository {

	Connection connection;

	public MapperRepository(Connection connection) {
		this.connection = connection;
	}
	public MapperRepository() {
		connection = DBUtil.getConnection();
	}

	/**
	 * Fetches the column name from the property file.
	 * @param entityName
	 * @return
	 */
	public String fetchColumnnames(String entityName) {
		Properties prop = new Properties();
		InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("/db.properties");
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			Logger.error("Error in fetching the Column names from the property file.", MapperRepository.class);
			e.printStackTrace();
		}
		String colNames = prop.getProperty(entityName);
		return colNames;
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
		Map<String, ArrayList<ColumnMapping>> mapper = new HashMap<String, ArrayList<ColumnMapping>>();
		try {
			//String colNames = fetchColumnnames(tableName);
			String colNames = "idmetabolitesUpload,acronym,synonym,additionalSynonym,formula,charge,molecularWeight,formationDeltaG,formationDeltaGUncertainity,splitSymbol,source";
			StringTokenizer colTokens = new StringTokenizer(colNames, ",");
			String batchIns = "insert into entity_mapping(workFlowId, entityName, dbColumn, fileColumn, mappingCount, mappingStatus ) values(?, ?, ?, ?, ?, ?)";
			PreparedStatement batchStatement = connection.prepareStatement(batchIns);
			while( colTokens.hasMoreTokens()) { 
				String colName = colTokens.nextToken();
				ArrayList<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>(); 
				for (java.util.Iterator<String> keys = contents.keySet().iterator(); keys.hasNext();) { 
					String fileCol = keys.next();
					StringTokenizer pStmtTokens = new StringTokenizer(contents.get(fileCol).getData(), ",");
					StringBuilder pStmtClause = new StringBuilder(" ");
					int matches = pStmtTokens.countTokens();
					if  ( matches > 0) {
						pStmtClause.append(" where " + colName + " in ( ?");
						for (int i = 2; i <= pStmtTokens.countTokens(); i++)
							pStmtClause.append(", ?");
						pStmtClause.append(")");
					} 
					String findMap = "select count(1) from " + tableName + pStmtClause.toString() ;
					PreparedStatement psCount = connection.prepareStatement(findMap);
					if (matches > 0) 
					for (int i = 1; pStmtTokens.hasMoreTokens(); i++)
						psCount.setString(i, pStmtTokens.nextToken());
					ResultSet rsCnt = psCount.executeQuery();
					rsCnt.next();
					columnMappings.add(new ColumnMapping(fileCol, rsCnt.getInt(1)));
					batchStatement.setString(2, tableName);
					batchStatement.setString(1, uId);
					batchStatement.setString(4, fileCol);
					batchStatement.setString(3, colName);
					batchStatement.setString(5, rsCnt.getString(1));
					int status = 0;
					if (fileCol.equals("splitField") && colName.equals("splitSymbol"))
							status = 1;	
					if (fileCol.equals("source") && colName.equals("source"))
						status = 1;
					batchStatement.setInt(6, status);
					batchStatement.addBatch(); 
				}
				mapper.put(colName, columnMappings);
			}
			batchStatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), MapperRepository.class);
			Logger.error("Issues in storing the mapping into the database.\n", MapperRepository.class);
		}
		return new TableMapper(entityType,uId, mapper);
	}

	/**
	 * Update table mappings
	 * @param mappings
	 */
	public boolean updateMapping(HashMap<String, String> mappings, String workFlowID) {
		String updateMap = "update entity_mapping set mappingStatus = 1 where dbColumn = ? and fileColumn = ? and workFlowId = ?";
		Iterator<String> keys = mappings.keySet().iterator();
		while(keys.hasNext()) {
			try {
				String key = keys.next();
				PreparedStatement updateStmt = connection.prepareStatement(updateMap);
				updateStmt.setString(1,  key);
				updateStmt.setString(2,  mappings.get(key));
				updateStmt.setString(3, workFlowID);
				updateStmt.executeUpdate();	
			} catch (SQLException e) {
				Logger.error(" Error while updating the mapping", MapperRepository.class);
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public void saveRawFileData(StringBuilder fileContents, String wId, String entityName) {
		String insRawData = "insert into entity_file(workflowId, rawContents, entityType) values(?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(insRawData);
			preparedStatement.setString(1, wId);
			preparedStatement.setString(2, fileContents.toString());
			preparedStatement.setString(3, entityName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			Logger.error(" Error in storing raw contents of the file to entity_data", MapperRepository.class);
			e.printStackTrace();
		}
		
	}
	
	public void saveFileData(String workFlowId, List<FileData> fileData) {
		String insRawData = "insert into entity_data(workflowId, columnData, columnName, rowId) values(?, ?, ?, ?)";
		try {
			PreparedStatement batchStatement = connection.prepareStatement(insRawData);
			for (int i = 0; i < fileData.size(); i++) {
				batchStatement.setString(1,workFlowId );
				batchStatement.setString(2, fileData.get(i).getColData());
				batchStatement.setString(3, fileData.get(i).getColName());
				batchStatement.setInt(4, fileData.get(i).getRowId());
				batchStatement.addBatch();
			}
			batchStatement.executeBatch();
		} catch(SQLException exception) {
			Logger.error(exception.getMessage(), MapperRepository.class);
			Logger.error("Exception occured while saving the file data to entity_data", MapperRepository.class);
		}
		
	}
	
	public boolean updateMetaInfoMapping(HashMap<String, String> mappings, 	String wId, String columnName) {
		Set<String> keys = mappings.keySet();
		String updateMetaInfo = "update entity_mapping set ";
		String whereClause = " where workFlowId = '" + wId + "' and fileColumn = '" + columnName + "'";
		StringBuilder updateClause = new StringBuilder();
		if (keys.contains("PP")) {
			updateClause.append(" PP = '" + mappings.get("PP") + "'");
			updateClause.append(" , PPSource = '" + mappings.get("PPSource") + "'");
			updateClause.append(" , PPpH = '" + mappings.get("PPpH") + "'");
		} 
		if (keys.contains("externalIdSource")) {
			if (updateClause.length() != 0) 
				updateClause.append(" , ");
			updateClause.append(" externalIdSource = '" + mappings.get("externalIdSource") + "'");
		} 
		if (keys.contains("splitSymbol")) {
			if (updateClause.length() != 0) 
				updateClause.append(" , ");
			updateClause.append(" splitSymbol  = '" + mappings.get("splitSymbol") + "'");
		}
		Statement updateStatement;
		try {
			updateStatement = connection.createStatement();
			Logger.info(updateMetaInfo + updateClause.toString() + whereClause, MapperRepository.class);
			updateStatement.executeUpdate(updateMetaInfo + updateClause.toString() + whereClause); 
		} catch (SQLException e) {
			Logger.error("error in updating the meta information of the entity_mappings", MapperRepository.class);
			e.printStackTrace();
			return false;
		}
		return true;
	}
}