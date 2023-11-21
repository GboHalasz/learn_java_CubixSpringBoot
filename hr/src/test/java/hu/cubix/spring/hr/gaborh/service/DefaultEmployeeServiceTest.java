package hu.cubix.spring.hr.gaborh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import hu.cubix.spring.hr.gaborh.model.Employee;

class DefaultEmployeeServiceTest {

	@Test
	void anEmployeeShouldGetFivePercentRaise() {
		// given
		Employee employee = new Employee(01L, "software developer", 100, LocalDateTime.of(1990, 01, 12, 8, 00));
		
		
		// when
		SalaryService salaryService = new SalaryService(new DefaultEmployeeService());
		salaryService.setNewSalaryForAn(employee);

		// then
		assertEquals(105, employee.getSalary());
	}

}
