package hu.cubix.spring.hr.gaborh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import hu.cubix.spring.hr.gaborh.model.Employee;

class SalaryServiceTest {
	
	class MockEmployeeServiceClass extends EmployeeSuperService{

		@Override
		public int getPayRaisePercent(Employee employee) {			
			return 10;
		}			
	}
	
	@Test
	void salaryServiceShouldSetsNewSalaryForTheGivenEmployeeUsingAnInjectedEmployeeServiceBean() {
		//given
		
		Employee employee = new Employee(01L, "Jane Doe", "software developer", 100, LocalDateTime.of(1990, 01, 12, 8, 00), null);			
		SalaryService salaryService = new SalaryService(new MockEmployeeServiceClass());
		//when
		
		salaryService.setNewSalaryForAn(employee);
		
		//then
		assertEquals(110, employee.getSalary());		
	}
	
}
