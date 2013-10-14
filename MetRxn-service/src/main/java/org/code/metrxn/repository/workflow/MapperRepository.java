package org.code.metrxn.repository.workflow;

import java.sql.Connection;
import java.util.List;
import org.code.metrxn.model.uploader.ColumnData;
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
	public TableMapper fetchDbMappings(String uId, String entityName, List<ColumnData> contents) {
		//TODO : call a procedure that fetches the mapping information.
		TableMapper tableMapper = null;
		return tableMapper;
	}
	
	public boolean updateMapping(TableMapper tableMapper) {
		return false;
	}
	
}
