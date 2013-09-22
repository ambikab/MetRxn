package org.code.metrxn.util;

/**
 * 
 * @author Ambika_b
 *
 */
public class SearchCriteria {
	
	String sortColumn;
	
	String sortOrder;
	
	String searchString;
	
	int reqPageNo;
	
	int numberOfRecords;
	
	public int getReqPageNo() {
		return reqPageNo;
	}

	public void setReqPageNo(int reqPageNo) {
		this.reqPageNo = reqPageNo;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
