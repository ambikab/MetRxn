package org.code.metrxn.service.workflow;

import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.multipart.FormDataParam;

/**
 * 
 * @author ambika_b
 *
 */
@Path("/entity/uploader/mapper")
public class MapperService {
/*
	static MapperRepository mapperRepository = new MapperRepository();

	*//**
	 * updates the mapping stored in the database.
	 * @param mappingData
	 *//*
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public String updateMappings(@FormDataParam("updatedMapping") String mappingData, @FormDataParam("sessionId") String uId) {
		Boolean updateMapping = false;
		//Convert the mappingData Json to TableMapper
		ObjectMapper mapper = new ObjectMapper();
		try {
			TableMapper tableMapper = mapper.readValue(mappingData, TableMapper.class);
			//updateMapping = mapperRepository.updateMapping(tableMapper);
		} catch (JsonParseException e) {
			System.out.println("Exception occured while converting into JSON");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println("Exception occured while converting into JSON");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception occured while converting into JSON");
			e.printStackTrace();
		}
		if (updateMapping)
			return "error in updating mappings";
		else
			return "sucessfully updated the mappings";
	}
*/
}
