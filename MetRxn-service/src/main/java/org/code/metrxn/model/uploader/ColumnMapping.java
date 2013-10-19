package org.code.metrxn.model.uploader;

/**
 * 
 * @author ambika_b
 *
 */

public class ColumnMapping {
	
	String colName;
	
	Integer count;
	
	public ColumnMapping() {
		
	}

	public ColumnMapping(String colName, Integer count) {
		super();
		this.colName = colName;
		this.count = count;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	} 
	
}
