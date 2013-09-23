function getEmployeeSearch(variable1) {
	return "select empId, empName from employee where empName like '" + variable1 + "'";
}