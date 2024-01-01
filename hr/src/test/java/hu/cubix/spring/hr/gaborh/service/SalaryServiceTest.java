package hu.cubix.spring.hr.gaborh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.Qualification;

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
		
		Position developer = new Position("software developer", Qualification.NONE, 20000);
		Employee employee = new Employee("Jane Doe", developer, 100, LocalDateTime.of(1990, 01, 12, 8, 00), null);			
		SalaryService salaryService = new SalaryService(new MockEmployeeServiceClass());
		//when
		
		salaryService.setNewSalaryForAn(employee);
		
		//then
		assertEquals(110, employee.getSalary());		
	}
	
}
