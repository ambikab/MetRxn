package org.code.metrxn.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.dto.ViewResource;
import org.code.metrxn.repository.QueryRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.SearchCriteria;

/**
 * 
 * @author ambika_b
 *
 */

@Path("/queries")
public class QueryExecutorService {

	QueryRepository queryRepository = new QueryRepository();
	///{pageNumber}/{sortCol}/{sortOrder}/{queryString}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/results")
	public String getPaginatedResults(@FormParam("pageNumber") String pageNumber, 
			@FormParam("sortCol") String sortCol,  
			@FormParam("sortOrder") String sortOrder,
			@FormParam("queryString") String searchString) throws IOException, SQLException {
		int reqPgNo = Integer.parseInt(pageNumber);
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setSearchString(URLDecoder.decode(searchString, "UTF-8"));
		searchCriteria.setSortColumn(sortCol);
		searchCriteria.setSortOrder(sortOrder);
		searchCriteria.setReqPageNo(reqPgNo);
		searchCriteria.setNumberOfRecords(5);
		int totalRecords = queryRepository.getTotalCount(searchCriteria);
		return JsonUtil.toJsonForObject(new ViewResource(queryRepository.fetchResults(searchCriteria), totalRecords, reqPgNo)).toString();
	}
}