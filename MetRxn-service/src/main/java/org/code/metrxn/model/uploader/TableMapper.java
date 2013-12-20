package org.code.metrxn.model.uploader;

import java.util.ArrayList;
import java.util.Map;

/**
 * Maps the columns in each file to 
 * specific columns in the database tables.
 * @author ambika_b
 *
 */

public class TableMapper {

	public String entityName;
	
	public String id;
	
	public Map<String, ArrayList<ColumnMapping>> mapper;
	
	public String sessionId; 

	public TableMapper() {
	}
	
	public TableMapper(String entityName, String id, Map<String, ArrayList<ColumnMapping>> mapper) {
		super();
		this.entityName = entityName;
		this.id = id;
		this.mapper = mapper;
	}
	
	public TableMapper(String entityName, String id, Map<String, ArrayList<ColumnMapping>> mapper, String sessionId) {
		super();
		this.entityName = entityName;
		this.id = id;
		this.mapper = mapper;
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public Map<String, ArrayList<ColumnMapping>> getMapper() {
		return mapper;
	}

	public void setMapper(Map<String, ArrayList<ColumnMapping>> mapper) {
		this.mapper = mapper;
	}
	
	
	
}
