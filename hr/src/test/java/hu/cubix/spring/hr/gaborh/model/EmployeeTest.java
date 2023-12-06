package hu.cubix.spring.hr.gaborh.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeTest {

	private Employee employeeUnderTest;

	@BeforeEach
	void setUp() throws Exception {
		employeeUnderTest = new Employee(01L, "Jane Doe", "software developer", 20000, LocalDateTime.of(1990, 01, 12, 8, 00));
	}

	@Test	
	void canBeCreatedEmployeeObject() {
		//given
		//when	
		//then
		assertTrue(employeeUnderTest instanceof Employee);
	}

	

}
