package org.code.metrxn.util;

import java.sql.*;
import java.io.*;
class SaveImageToDatabase {
	public static void main(String[] args) throws SQLException {
		Connection connection = null;
		String connectionURL = "jdbc:mysql://costas4086.engr.psu.edu:3306/test_Metrxn_version_2";
		PreparedStatement psmnt = null;
		FileInputStream fis;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "ambika", "ambika");
			File image = new File("C:/pictures/nad.jpg");
			psmnt = connection.prepareStatement
					("update FormulaChargeDistribution set images = ? where idFormulaChargeDistribution = ?");
			psmnt.setString(2,"1");
			fis = new FileInputStream(image);
			psmnt.setBinaryStream(1, (InputStream)fis, (int)(image.length()));
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
