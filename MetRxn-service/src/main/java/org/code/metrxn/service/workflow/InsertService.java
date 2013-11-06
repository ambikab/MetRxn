package org.code.metrxn.service.workflow;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.code.metrxn.repository.dml.InsertRepository;
import org.code.metrxn.util.Logger;

@Path("/entity/data/migrate")
public class InsertService {

	InsertRepository insertRepository = new InsertRepository();
	
	@POST
	public void insertEntityData(@FormParam("workFlowId")String workFlowId) {
		Logger.info("Entity data being populated!!", InsertService.class);
		insertRepository.metabolitesUpload(workFlowId);
	}

}
