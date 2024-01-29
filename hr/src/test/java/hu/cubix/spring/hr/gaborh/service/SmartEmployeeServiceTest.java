package hu.cubix.spring.hr.gaborh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.Qualification;

@SpringBootTest
class SmartEmployeeServiceTest {

	@Autowired
	SalaryService salaryService;

	@Test
	void employeesShouldReceiveDifferentPercentRaisesDependingOnYearsTheySpentAtTheCompany() {
		// given

		Position developer = new Position("software developer", Qualification.NONE);
		Employee employee13 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusYears(13), null, null);
		Employee employee10 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusYears(10), null, null);
		Employee employee7 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusYears(7), null, null);
		Employee employee5 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusYears(5), null, null);
		Employee employee3 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusYears(3), null, null);
		Employee employee2_5 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusMonths(30), null, null);
		Employee employee2 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusYears(2), null, null);
		Employee employee1 = new Employee("Jane Doe", developer, 100, LocalDateTime.now().minusYears(1), null, null);

		// when
		// SalaryService salaryService = new SalaryService(new SmartEmployeeService());
		salaryService.setNewSalaryForAn(employee13);
		salaryService.setNewSalaryForAn(employee10);
		salaryService.setNewSalaryForAn(employee7);
		salaryService.setNewSalaryForAn(employee5);
		salaryService.setNewSalaryForAn(employee3);
		salaryService.setNewSalaryForAn(employee2_5);
		salaryService.setNewSalaryForAn(employee2);
		salaryService.setNewSalaryForAn(employee1);

		// then
		assertEquals(110, employee13.getSalary());
		assertEquals(110, employee10.getSalary());
		assertEquals(105, employee7.getSalary());
		assertEquals(105, employee5.getSalary());
		assertEquals(102, employee3.getSalary());
		assertEquals(102, employee2_5.getSalary());
		assertEquals(100, employee2.getSalary());
		assertEquals(100, employee1.getSalary());
	}

}
