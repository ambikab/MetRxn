package org.code.metrxn.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/results/{pageNumber}/{sortCol}/{sortOrder}/{queryString}")
	public String getPaginatedResults(@PathParam("pageNumber") String pageNumber, 
												  @PathParam("sortCol") String sortCol,  
												  @PathParam("sortOrder") String sortOrder,
												  @PathParam("queryString") String searchString) throws IOException, SQLException {
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