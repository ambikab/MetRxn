package org.code.metrxn.model.uploader;

/**
 * @author ambika_b
 *
 */

public class ColumnMapping {
	
	public String colName;
	
	public Integer count;
	
	public Boolean status;
	
	public ColumnMapping() {
		
	}

	public ColumnMapping(String colName, Integer count, Boolean status) {
		super();
		this.colName = colName;
		this.count = count;
		this.status = status;
	}

	public ColumnMapping(String colName, Integer count) {
		super();
		this.colName = colName;
		this.count = count;
		this.status = false;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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
