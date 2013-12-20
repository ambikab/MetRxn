package org.code.metrxn.authenticate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.code.metrxn.model.authenticate.User;
import org.code.metrxn.repository.authenticate.SessionRepository;
import org.code.metrxn.repository.authenticate.UserRepository;
import org.code.metrxn.service.authentication.AuthenticateUserService;
import org.code.metrxn.util.Logger;
/**
 * 
 * @author ambika babuji
 *
 */
public class AuthenticateServiceTest {

	static Connection connection;
	
	static UserRepository userRepository;
	
	static SessionRepository sessionRepository;
	
	static {
		createConnection();
	}
	
	public static void createConnection() {
		String connectionURL = "jdbc:mysql://costas4086.engr.psu.edu:3306/test_Metrxn_version_2";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "ambika", "ambika");
			userRepository = new UserRepository(connection);
			sessionRepository = new SessionRepository(connection);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void loginTest() {
		User newUser = new User("ambika","password1");
		userRepository.addUser(newUser);
		Logger.info("new user added to the database", AuthenticateServiceTest.class);
		AuthenticateUserService authenticateUser = new AuthenticateUserService(userRepository, sessionRepository);
		String result = authenticateUser.authenticateUser(newUser.getUserName(), newUser.getPassword());
		Logger.info("the result of authenticate user is : " + result, AuthenticateServiceTest.class);
	}
	
	public static void main(String[] args) {
		loginTest();
	}
}