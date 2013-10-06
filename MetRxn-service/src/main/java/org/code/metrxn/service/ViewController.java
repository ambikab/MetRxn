package org.code.metrxn.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.code.metrxn.model.Image;
import org.code.metrxn.repository.ImageRepository;

/**
 * 
 * @author ambika_b
 *
 */
@Path("/image")
public class ViewController {
	
	ImageRepository imageRepository = new ImageRepository();
	
	@GET
	@Produces("image/jpg")
	@Path("/{name}")
	public void getImages(@PathParam("name") String imageName, @Context HttpServletResponse response)  {
		BufferedInputStream bis;
		try {
			Image image = imageRepository.getImageByName(imageName);
			bis = new BufferedInputStream(image.getImage());
			response.setContentType("image/jpg");
			response.setHeader("Content-Disposition", "inline; filename=\"" + image.getName() + ".jpg\"");
			BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
			for (int data; (data = bis.read()) > -1;) {
				output.write(data);
			} 
		} catch (SQLException sql) {
			sql.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}