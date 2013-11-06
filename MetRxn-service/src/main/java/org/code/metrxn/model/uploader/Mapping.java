package org.code.metrxn.model.uploader;

/**
 * Holds the right mapping Objects
 * @author ambikaBabuji
 *
 */
public class Mapping {
	
	public String tableName;
	
	public String colName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Mapping(String tableName, String colName) {
		super();
		this.tableName = tableName;
		this.colName = colName;
	}

}
