package org.code.metrxn.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.code.metrxn.dto.ImageResource;
import org.code.metrxn.model.Image;
import org.code.metrxn.repository.ImageRepository;
import org.code.metrxn.repository.authenticate.SessionRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 
 * @author ambika_b
 *
 */

@Path("/image")
public class ImageService {

	ImageRepository imageRepository = new ImageRepository();

	SessionRepository sessionRepository = new SessionRepository();
	
	/**
	 * 
	 * @param imageName
	 * @return
	 * @throws IOException 
	 */
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	public String getImage(@FormParam("inputSQL") String inputSQL, @FormParam("sessionId") String sessionId) throws IOException {
		ImageResource imageResource = new ImageResource(null, null);
		if (!sessionRepository.isValidSession(sessionId))
			return JsonUtil.toJsonForObject(imageResource).toString();
		imageResource.setSessionId(sessionId);
		Image image = imageRepository.getImageByName(inputSQL);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(image.getImage());
		byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum =bis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); 
            }
        } catch (IOException ex) {
        	Logger.error("Exception while reading images", ImageService.class);
        }
        byte[] bytes = bos.toByteArray();
        bos.close();
        imageResource.setImage(Base64.encode(bytes));
        return JsonUtil.toJsonForObject(imageResource).toString();
	}
}