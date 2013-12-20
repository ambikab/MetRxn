package org.code.metrxn.service;

import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.dto.ViewResource;
import org.code.metrxn.repository.QueryRepository;
import org.code.metrxn.repository.authenticate.SessionRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.SearchCriteria;

/**
 * 
 * @author ambika_b
 * REST end point that receives for all the searches in the system.
 * 
 */

@Path("/queries")
public class QueryExecutorService {

	QueryRepository queryRepository = new QueryRepository();
	
	SessionRepository sessionRepository = new SessionRepository();
	
	/**
	 * Fetches the result set of the employees table, that matches the given search query.
	 * Returns the result set and the requested page number and
	 * total number of records that matched the query string.
	 * Result sets are converted to JSON.
	 * @param pageNumber
	 * @param sortCol
	 * @param sortOrder
	 * @param searchString
	 * @return json object of {@link ViewResource}
	 * @throws IOException
	 * @throws SQLException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/results")
	public String getPaginatedResults(@FormParam("sessionId")String sessionId, @FormParam("pageNumber") String pageNumber, 
			@FormParam("sortCol") String sortCol,  @FormParam("sortOrder") String sortOrder, @FormParam("queryString") String searchString) throws IOException, SQLException {
		if(!sessionRepository.isValidSession(sessionId))
			return JsonUtil.toJsonForObject(new ViewResource(null, -1,-1, null)).toString();
		int reqPgNo = Integer.parseInt(pageNumber);
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setSearchString(searchString);
		searchCriteria.setSortColumn(sortCol);
		searchCriteria.setSortOrder(sortOrder);
		searchCriteria.setReqPageNo(reqPgNo);
		searchCriteria.setNumberOfRecords(5);
		int totalRecords = queryRepository.getTotalCount(searchCriteria);
		return JsonUtil.toJsonForObject(new ViewResource(queryRepository.fetchResults(searchCriteria), totalRecords, reqPgNo, sessionId)).toString();
	}
}