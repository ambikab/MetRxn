package org.code.metrxn.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.code.metrxn.model.Employee;
import org.code.metrxn.util.DBUtil;
import org.code.metrxn.util.SearchCriteria;

/**
 * Handles the CRUD operations for the image object
 * @author ambika_b
 *
 */

public class EmployeeRepository {

	//TODO: connection pool to be configured.
	Connection connection;

	public EmployeeRepository() {
		connection = DBUtil.getConnection();
	}

	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from employee");
			while (rs.next()) {
				Employee employee = new Employee();
				employee.setName(rs.getString("empName"));
				employee.setId(rs.getInt("empId"));
				employees.add(employee);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employees;
	}

	public int getTotalRecordCount( SearchCriteria searchCriteria){
		int count = 0;
		try {
			Statement statement = connection.createStatement();
			String searchString = "%" + searchCriteria.getSearchString() + "%";
			ResultSet rs = statement.executeQuery("select count(1) from employee where empName like '" 
							+ searchString 
							+ "'");
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<Employee> getEmployeesPaginated(int reqPgNo, int totalRecords, SearchCriteria searchCriteria) throws IOException {
		Properties prop = new Properties();
		InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("/db.properties");
		prop.load(inputStream);
		String searchString = "%" + searchCriteria.getSearchString() + "%";
		int tableLoad = Integer.parseInt(prop.getProperty("RECORDS_PER_PAGE"));
		if (((reqPgNo - 1) * tableLoad) + 1  > totalRecords )
			return null;
		List<Employee> employees = new ArrayList<Employee>();
		try {
			Statement statement = connection.createStatement();
			int offset = ((reqPgNo - 1) * tableLoad) ;
			String query = "select * from employee where empName like '" 
							+ searchString 
							+ "' order by "	+ searchCriteria.getSortColumn() + " " + searchCriteria.getSortOrder() 
							+ " limit " + offset + " , " + tableLoad;
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Employee employee = new Employee();
				employee.setName(rs.getString("empName"));
				employee.setId(rs.getInt("empId"));
				employees.add(employee);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employees;
	}

	public Employee getEmployeeById(int empId) throws SQLException {
		String selectSQL = "select * from employee where empId = " + empId +"";
		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
		ResultSet rs = preparedStatement.executeQuery(selectSQL);
		rs.next();
		Employee employee = new Employee();
		employee.setId(rs.getInt("empId"));
		employee.setName(rs.getString("empName"));
		return employee;
	}

	public Employee getEmployeeByQuery(String selectSQL) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
		ResultSet rs = preparedStatement.executeQuery(selectSQL);
		rs.next();
		Employee employee = new Employee();
		employee.setId(rs.getInt("empId"));
		employee.setName(rs.getString("empName"));
		return employee;
	}

	public void addEmployee(Employee employee) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into employee(empName,empId) values (?, ? )");
			preparedStatement.setString(1, employee.getName());
			preparedStatement.setInt(2, employee.getId());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}