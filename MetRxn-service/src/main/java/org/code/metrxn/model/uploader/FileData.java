package org.code.metrxn.model.uploader;

/**
 * 
 * @author ambika_b
 *
 */
public class FileData {

	String colName;
	
	String colData;
	
	int rowId;
	
	public FileData() {
		
	}
	
	public FileData(String colName, String colData, int rowId) {
		super();
		this.colName = colName;
		this.colData = colData;
		this.rowId = rowId;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColData() {
		return colData;
	}

	public void setColData(String colData) {
		this.colData = colData;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	
}
