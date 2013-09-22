package org.code.metrxn.service;

import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.dto.Employees;
import org.code.metrxn.dto.PaginationResource;
import org.code.metrxn.repository.EmployeeRepository;
import org.code.metrxn.util.SearchCriteria;

@Path("/employees")
public class EmployeeService {

	private EmployeeRepository employeeRepository;

	public EmployeeService() {
		super();
		employeeRepository = new EmployeeRepository();
	}
	
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getEmployee(@PathParam(value = "id") String id) throws NumberFormatException, SQLException {
		return employeeRepository.getEmployeeById(Integer.parseInt(id)).toString();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Employees getEmployee() throws NumberFormatException, SQLException {
		Employees employees = new Employees(employeeRepository.getEmployees());
		return employees;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/paginated/{pageNumber}/{sortCol}/{sortOrder}/{searchString}")
	public PaginationResource getPaginatedResults(@PathParam("pageNumber") String pgNo, 
												  @PathParam("sortCol") String sortCol,  
												  @PathParam("sortOrder") String sortOrder,
												  @PathParam("searchString") String searchString) throws IOException {
		int reqPgNo = Integer.parseInt(pgNo);
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setSearchString(searchString);
		searchCriteria.setSortColumn(sortCol);
		searchCriteria.setSortOrder(sortOrder);
		int totalRecords = employeeRepository.getTotalRecordCount(searchCriteria);
		Employees resultEmployees = new Employees(employeeRepository.getEmployeesPaginated(reqPgNo, totalRecords, searchCriteria));
		PaginationResource paginationResource = new PaginationResource(resultEmployees, reqPgNo, totalRecords); 
		return paginationResource;
	}
}