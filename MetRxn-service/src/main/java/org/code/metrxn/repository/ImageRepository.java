package org.code.metrxn.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.code.metrxn.model.Image;
import org.code.metrxn.util.DBUtil;

public class ImageRepository {
	
	Connection connection;

	public ImageRepository() {
		connection = DBUtil.getConnection();
	}
	
	public Image getImageByName(String selectSQL) {
		//String selectSQL = "select * from FormulaChargeDistribution where idFormulaChargeDistribution = '" + imageName +"'";
		System.out.println(selectSQL);
		Image image = null;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			rs.next();
			image = new Image();
			image.setImage(rs.getBinaryStream("images"));
			image.setName("resultImage");
		} catch (SQLException e) {
			System.out.println("Issue in fetching the image for database\n");
		}
		return image;
	}
}
