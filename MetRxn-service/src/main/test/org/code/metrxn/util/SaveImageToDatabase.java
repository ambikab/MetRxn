package org.code.metrxn.util;

import java.sql.*;
import java.io.*;
class SaveImageToDatabase {
	public static void main(String[] args) throws SQLException {
		Connection connection = null;
		String connectionURL = "jdbc:mysql://localhost:3306/images";
		PreparedStatement psmnt = null;
		FileInputStream fis;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "root", "root");
			File image = new File("C:/pictures/fish.jpg");
			psmnt = connection.prepareStatement
					("insert into images "+ "values(?,?)");
			psmnt.setString(1,"fish");
			fis = new FileInputStream(image);
			psmnt.setBinaryStream(2, (InputStream)fis, (int)(image.length()));
			int s = psmnt.executeUpdate();
			if(s>0) {
				System.out.println("Uploaded successfully !");
			}
			else {
				System.out.println("unsucessfull to upload image.");
			}
		}
		// catch if found any exception during rum time.
		catch (Exception ex) {
			System.out.println("Found some error : "+ex);
		}
		finally {
			// close all the connections.
			connection.close();
			psmnt.close();
		}
	}
}
