package org.code.metrxn.service.workflow;

import java.util.HashMap;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.code.metrxn.repository.authenticate.SessionRepository;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.repository.workflow.SBMLRepository;
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

	static SBMLRepository sbmlRepository = new SBMLRepository();

	static SessionRepository sessionRepository = new SessionRepository();

	/**
	 * updates the mapping stored in the database.
	 * @param mappingData
	 *  
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public String updateMappings(@FormParam("updatedMapping") String mappingData, @FormParam("workflowId") String uId, @FormParam("sessionId") String sessionId)  {
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("sessionId", null);
		response.put("Result", "Please log in to continue.");
		response.put("status", "ERROR");
		if (!sessionRepository.isValidSession(sessionId)) {
			return JsonUtil.toJsonForObject(response).toString();
		}
		
		response.put("sessionId", sessionId);
		try {
			HashMap<String, String> mappings = new HashMap<String, String>();
			JSONObject jsonObj = new JSONObject(mappingData);
			mappings = JsonUtil.jsonToString(jsonObj, mappings);
			if (mapperRepository.updateMapping(mappings, uId)) {
				response.put("status", "SUCCESS");
				response.put("result", "sucessfully updated the mappings");
			}
		} catch (JSONException e) {
			response.put("Result", "Exception occured while converting into JSON");
			response.put("status", "ERROR");
			Logger.error("Exception occured while converting into JSON", MapperService.class);
			e.printStackTrace();
		}
		
		return JsonUtil.toJsonForObject(response).toString();
	}

	@Path("/metaData")
	@POST
	public String updateMetaInfoMapping(@FormParam("metaData") String metaData, @FormParam("sessionId") String sessionId, @FormParam("workflowId") String wId, @FormParam("columnName") String columnName) {
		HashMap<String, String> response = new HashMap<String, String>();
		if (! sessionRepository.isValidSession(sessionId)) {
			response.put("sessionId", null);
			response.put("Result", "Please log in to continue.");
			response.put("status", "ERROR");
			return JsonUtil.toJsonForObject(response).toString();
		}
		
		response.put("sessionId", sessionId);
		try {
			HashMap<String, String> mappings = new HashMap<String, String>();
			JSONObject jsonObj = new JSONObject(metaData);
			mappings = JsonUtil.jsonToString(jsonObj, mappings);
			if(mapperRepository.updateMetaInfoMapping(mappings, wId, columnName)){
				response.put("status", "SUCCESS");
				response.put("result", "sucessfully updated the mappings");
			}
		} catch (JSONException e) {
			Logger.error("Exception occured while converting into JSON", MapperService.class);
			e.printStackTrace();
			response.put("Result", "Exception occured while in returning the response as JSON.");
			response.put("status", "ERROR");
		}
		
		return JsonUtil.toJsonForObject(response).toString();
	}

	@Path("/sbml")
	@POST
	public String confirmMappings(@FormParam("workflowId") String workflowId, @FormParam("sessionId") String sessionId) {
		HashMap<String, String> response = new HashMap<String, String>();
		if (! sessionRepository.isValidSession(sessionId)) {
			response.put("sessionId", null);
			response.put("Result", "Please log in to continue.");
			response.put("status", "ERROR");
			return JsonUtil.toJsonForObject(response).toString();
		}
		
		response.put("sessionId", sessionId);
		if (sbmlRepository.updateStatus(workflowId)) {
			response.put("Result", "Update successful.");
			response.put("status", "SUCCESS");
		} else {
			response.put("Result", "Error in updating.");
		}
		
		return JsonUtil.toJsonForObject(response).toString();
	}
	
}