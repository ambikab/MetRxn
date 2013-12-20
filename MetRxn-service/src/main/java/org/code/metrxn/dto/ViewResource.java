package org.code.metrxn.dto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Wraps up the result set data that is to be rendered to the requested object.
 * Contains the result set fetched from the database, along with the information of pagination 
 * and the total number of records that matched the current query.
 * @author ambika_b
 *
 */
public class ViewResource {
	
	public List<LinkedHashMap<String,Object>> resultSet;
	
	public int totalRecordCount;
	
	public int currentPageNumber;
	
	public String sessionId;

	public ViewResource() {
	}
	
	public ViewResource(List<LinkedHashMap<String,Object>> resultSet, int totalRecordCount, int currentPageNumber, String sessionId) {
		super();
		this.resultSet = resultSet;
		this.totalRecordCount = totalRecordCount;
		this.currentPageNumber = currentPageNumber;
		this.sessionId = sessionId;
	}

	public void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}
	
	public String getSessionId() {
		return this.sessionId;
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

	public List<LinkedHashMap<String, Object>> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<LinkedHashMap<String, Object>> resultSet) {
		this.resultSet = resultSet;
	}
	
}
