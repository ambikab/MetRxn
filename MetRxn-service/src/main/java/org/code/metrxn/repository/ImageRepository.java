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
	
	public Image getImageByName(String imageName) throws SQLException {
		String selectSQL = "select * from images where imageName = '" + imageName +"'";
		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
		ResultSet rs = preparedStatement.executeQuery(selectSQL);
		rs.next();
		Image image = new Image();
		image.setImage(rs.getBinaryStream("image"));
		image.setName(rs.getString("imageName"));
		return image;
	}
}
