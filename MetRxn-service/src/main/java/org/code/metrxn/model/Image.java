package org.code.metrxn.model;

import java.io.InputStream;

public class Image {
	private String name;
	private InputStream image;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InputStream getImage() {
		return image;
	}
	public void setImage(InputStream image) {
		this.image = image;
	}
	
}
