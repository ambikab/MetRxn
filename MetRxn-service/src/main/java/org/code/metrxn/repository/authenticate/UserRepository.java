package org.code.metrxn.repository.authenticate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.code.metrxn.model.authenticate.User;
import org.code.metrxn.util.DBUtil;
import org.code.metrxn.util.Logger;

/**
 * 
 * @author ambika babuji
 *
 */
public class UserRepository {

	Connection connection;

	public UserRepository() {
		super();
		connection = DBUtil.getConnection();
	}

	public UserRepository(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public User fetchUser(String userName, String password){
		String checkUser = "select userName, password from user where userName = ? and password = ? ";
		User loggedUser = null;
		try {
			PreparedStatement checkUserStmt= connection.prepareStatement(checkUser);
			checkUserStmt.setString(1, userName);
			checkUserStmt.setString(2, password);
			ResultSet resultSet = checkUserStmt.executeQuery();
			while(resultSet.next()) {
				loggedUser = new User();
				loggedUser.setUserName(resultSet.getString(1));
				loggedUser.setPassword(resultSet.getString(2));
			}
		} catch (SQLException e) {
			Logger.error("Error in fetching the details of the user!", UserRepository.class);
			e.printStackTrace();
		}
		return loggedUser;
	}
	
	public boolean addUser(User newUser) {
		String addUser = "insert into user(userName, password) values(?, ?)";
		try {
			PreparedStatement addUserStmt = connection.prepareStatement(addUser);
			addUserStmt.setString(1, newUser.getUserName());
			addUserStmt.setString(2, newUser.getPassword());
			addUserStmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}