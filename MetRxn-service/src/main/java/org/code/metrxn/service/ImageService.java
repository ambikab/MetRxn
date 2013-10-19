package org.code.metrxn.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.model.Image;
import org.code.metrxn.repository.ImageRepository;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 
 * @author ambika_b
 *
 */

@Path("/image")
public class ImageService {

	ImageRepository imageRepository = new ImageRepository();

	/**
	 * 
	 * @param imageName
	 * @return
	 * @throws IOException 
	 */
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	public String getImage(@FormParam("inputSQL") String inputSQL) throws IOException {
		Image image = imageRepository.getImageByName(inputSQL);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(image.getImage());
		byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum =bis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); 
                //System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
        	System.out.println("Exception while reading images");
        }
        byte[] bytes = bos.toByteArray();
        bos.close();
        return Base64.encode(bytes);
	}

/*	@GET
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
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}*/

}