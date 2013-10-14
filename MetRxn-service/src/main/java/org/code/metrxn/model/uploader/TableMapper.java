package org.code.metrxn.model.uploader;

import java.util.Map;

/**
 * Maps the columns in each file to 
 * specific columns in the database tables.
 * @author ambika_b
 *
 */
public class TableMapper {

	String entityName;
	
	String id;

	/**
	 * file column to database table column mappings
	 * Key is the file column and the value is the database column. 
	 * TODO : concur
	 */
	Map<String, String> mapper;

	public TableMapper() {
	}
	
	public TableMapper(String entityName, String id, Map<String, String> mapper) {
		super();
		this.entityName = entityName;
		this.id = id;
		this.mapper = mapper;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getMapper() {
		return mapper;
	}

	public void setMapper(Map<String, String> mapper) {
		this.mapper = mapper;
	}
	
	
	
}
