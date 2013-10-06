package org.code.metrxn.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Corresponds to the employee table in the database.
 * @author ambika_b
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "employee")
public class Employee {
	
	public String name;
	
	public int id;
	
	public Employee() {
		
	}
	
	public Employee( String name, int id) {
		this.id = id;
		this.name = name;
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}