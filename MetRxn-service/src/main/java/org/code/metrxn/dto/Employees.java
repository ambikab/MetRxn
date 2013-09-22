package org.code.metrxn.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.code.metrxn.model.Employee;

/**
 * 
 * @author ambika_b
 *
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
