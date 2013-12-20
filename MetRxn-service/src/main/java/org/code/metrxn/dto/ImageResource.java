package org.code.metrxn.dto;

/**
 * 
 * @author ambika_b
 *
 */
public class ImageResource {

	public String image;
	
	public String sessionId;

	public ImageResource(){
		
	}
	
	public ImageResource(String image, String sessionId) {
		super();
		this.image = image;
		this.sessionId = sessionId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}