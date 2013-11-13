package org.code.metrxn.enums;

/**
 * Maps a given name to the actual 
 * table in the database.
 * @author ambika_b
 *
 */
public enum TableMap {

	METABOLITE("metabolite", "metabolitesUpload"), REACTIONS("rections", "reactionsUpload");
	
	private String fileValue;
	
	private String tableValue;
	
	private TableMap(String fileValue, String tableValue) {
		this.tableValue = tableValue;
		this.fileValue = fileValue;
	}
	
	public String fileValue() {return fileValue; }
	
	public String tableValue() { return tableValue; }
}
