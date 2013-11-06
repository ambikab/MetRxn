package org.code.metrxn.service.workflow;

import java.util.HashMap;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


/**
 * 
 * @author ambika_b
 *
 */

@Path("/entity/uploader/mapper")
public class MapperService {

	static MapperRepository mapperRepository = new MapperRepository();

	/**
	 * updates the mapping stored in the database.
	 * @param mappingData
	 *  
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public String updateMappings(@FormParam("updatedMapping") String mappingData, @FormParam("sessionId") String uId)  {
		Boolean updateMapping = false;
		HashMap<String, String> mappings = new HashMap<String, String>();
		try {
			JSONObject jsonObj = new JSONObject(mappingData);
			Logger.info("Json string is :" + mappingData, MapperService.class);
			mappings = JsonUtil.jsonToString(jsonObj, mappings);
			updateMapping = mapperRepository.updateMapping(mappings, uId);
		} catch (JSONException e) {
			Logger.error("Exception occured while converting into JSON", MapperService.class);
			e.printStackTrace();
		}
		if (! updateMapping)
			return "error in updating mappings";
		else
			return "sucessfully updated the mappings";
	}
	
	@Path("/metaData")
	@POST
	public String updateMetaInfoMapping(@FormParam("metaData") String metaData, @FormParam("workflowId") String wId, @FormParam("columnName") String columnName) {
		boolean updateMetaInfo = false;
		HashMap<String, String> mappings = new HashMap<String, String>();
		try {
			JSONObject jsonObj = new JSONObject(metaData);
			mappings = JsonUtil.jsonToString(jsonObj, mappings);
			updateMetaInfo = mapperRepository.updateMetaInfoMapping(mappings, wId, columnName);
		} catch (JSONException e) {
			Logger.error("Exception occured while converting into JSON", MapperService.class);
			e.printStackTrace();
		}
		if (! updateMetaInfo)
			return "error in updating mappings";
		else
			return "sucessfully updated the mappings";
	}
}