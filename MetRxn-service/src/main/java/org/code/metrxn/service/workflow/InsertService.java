package org.code.metrxn.service.workflow;

import java.util.HashMap;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.code.metrxn.repository.authenticate.SessionRepository;
import org.code.metrxn.repository.dml.InsertRepository;
import org.code.metrxn.util.JsonUtil;

@Path("/entity/data/migrate")
public class InsertService {

	InsertRepository insertRepository = new InsertRepository();
	
	SessionRepository sessionRepository = new SessionRepository();
	
	@POST
	public String insertEntityData(@FormParam("workFlowId")String workFlowId, @FormParam("sessionId")String sessionId) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("Result", "ERROR");
		hashMap.put("status", "Please log in to continue.");
		if(!sessionRepository.isValidSession(sessionId)) {
			return JsonUtil.toJsonForObject(hashMap).toString();
		}
		hashMap.put("sessionId", sessionId);
		insertRepository.metabolitesUpload(workFlowId);
		hashMap.put("status", "SUCCESS");
		hashMap.put("Result", "Data is saved successfully!");
		return JsonUtil.toJsonForObject(hashMap).toString();
	}
}