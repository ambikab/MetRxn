package org.code.metrxn.dto;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author ambika_b
 *
 */
public class ViewResource {
	
	public List<HashMap<String,Object>> resultSet;
	
	public int totalRecordCount;
	
	public int currentPageNumber;

	public ViewResource() {
	}
	
	public ViewResource(List<HashMap<String,Object>> resultSet, int totalRecordCount, int currentPageNumber) {
		super();
		this.resultSet = resultSet;
		this.totalRecordCount = totalRecordCount;
		this.currentPageNumber = currentPageNumber;
	}

	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public List<HashMap<String, Object>> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<HashMap<String, Object>> resultSet) {
		this.resultSet = resultSet;
	}
	
}
