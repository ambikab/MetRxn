package org.code.metrxn.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.code.metrxn.model.Employee;

/**
 * 
 * @author ambika_b
 * Contains the list of {@link Employee}
 * This is used as a wrapper for making the required conversions on the employee object
 * as rendered to the requested resource.
 */

@XmlRootElement
public class Employees {

	
	List<Employee> employees;
	
	public Employees() {
	}
	
	public Employees(List<Employee> employees) {
		this.employees = employees;
	}


	@XmlElement(name = "employees", type = Employee.class)
	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
}
