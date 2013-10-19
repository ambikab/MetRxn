package org.code.metrxn.model.uploader;

/**
 * Contians the data from each columns from the file.
 * @author ambika_b
 *
 */
public class FileColumn {
	
	String id;
	
	String name;
	
	String data; //CSV of column values

	public FileColumn() {
		super();
	}

	public FileColumn(String id, String name, String data) {
		super();
		this.id = id;
		this.name = name;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
