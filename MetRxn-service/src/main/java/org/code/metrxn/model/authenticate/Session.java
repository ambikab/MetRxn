package org.code.metrxn.model.authenticate;

import java.sql.Date;

/**
 * 
 * @author ambika babuji
 *
 */
public class Session {

	public String sessionId;
	
	public boolean active;
	
	public User loggedUser;
	
	public Date lastModifiedDate;
	
	public Session() {
		
	}
	
	public Session(String sessionId, boolean active, User loggedUser, Date lastModifiedDate){
		this.active = active;
		this.lastModifiedDate = lastModifiedDate;
		this.sessionId = sessionId;
		this.loggedUser = loggedUser;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}
