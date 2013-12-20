package org.code.metrxn.model.authenticate;

import java.util.ArrayList;

/**
 * Model object that holds the details of the User.
 * @author ambika_b
 *
 */
public class User {

	public String userName;
	
	public String password;
	
	public String activationToken;
	
	public boolean isActive;
	
	ArrayList<String> roles;

	public User() {
		super();
	}

	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	
	public User(String userName, String password, String activationToken, boolean isActive) {
		super();
		this.userName = userName;
		this.password = password;
		this.isActive = isActive;
		this.activationToken = activationToken;
	}
	
	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

}