package hu.cubix.spring.hr.gaborh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.cubix.spring.hr.gaborh.model.Employee;

class EmployeeServiceTest {
	
	class MockClass extends EmployeeSuperService{

		@Override
		public int getPayRaisePercent(Employee employee) {
			
			return 0;
		}			
	}
	private MockClass mockObject;

	
	
	@BeforeEach
	void beforeEachTestMethod() {
		mockObject = new MockClass();
	}
	
	@Test	
	void employeeServiceInterfaceCanBeImplemented() {
		//given
		
		//when
		
		//then
		assertTrue(mockObject instanceof EmployeeService);
	}

	@Test
	void getPayRaisePercentMethodShouldReturnsWhatPercentagePayRaiseShouldTheGivenEmployeeGet() {
		//given
		Employee employee = new Employee(01L, "Jane Doe", "software developer", 20000, LocalDateTime.of(1990, 01, 12, 8, 00), null);
		
		//when
		int percent = mockObject.getPayRaisePercent(employee);
		//then
		assertEquals(0, percent);
	}
}
