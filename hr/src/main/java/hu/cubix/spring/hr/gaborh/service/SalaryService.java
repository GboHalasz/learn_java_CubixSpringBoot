package hu.cubix.spring.hr.gaborh.service;

import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Employee;

@Service
public class SalaryService {
	
	private EmployeeService employeeService;
	
	public SalaryService(EmployeeService employeeService) {		
		this.employeeService = employeeService;
	}

	
	
	public void setNewSalaryForAn(Employee employee) {
	Integer newSalary = employee.getSalary() + employee.getSalary() * employeeService.getPayRaisePercent(employee) / 100;
	
 		employee.setSalary(newSalary);		
	}

}
