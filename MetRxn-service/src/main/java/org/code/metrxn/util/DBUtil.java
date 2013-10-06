package org.code.metrxn.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * utility to manage connections to the database.
 * @author ambika_b
 *
 */
public class DBUtil {
	
	private static Connection connection = null;

	public static Connection getConnection() {
		if (connection != null)
			return connection;
		else {
			try {
				Properties prop = new Properties();
				InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("/db.properties");
				prop.load(inputStream);
				String driver = prop.getProperty("driver");
				String url = prop.getProperty("url");
				String user = prop.getProperty("user");
				String password = prop.getProperty("password");
				Class.forName(driver);
				connection = DriverManager.getConnection(url, user, password);
				Statement statement=  connection.createStatement();
				statement.executeUpdate("set group_concat_max_len = 1500000");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return connection;
		}

	}
}

