package hu.cubix.spring.hr.gaborh.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HrEmployeesRestControllerIT {

	private static final String API_EMPLOYEES = "/api/employees";

	@Autowired
	WebTestClient webTestClient;

	private List<EmployeeDto> employeesBefore;
	private long newId = 100L;
	private EmployeeDto newEmployee;

	@BeforeEach
	void setUp() {
		employeesBefore = getAllEmployees();
		if (employeesBefore.size() > 0) {
			newId = employeesBefore.get((employeesBefore.size() - 1)).getId() + 1;
		}
		
		newEmployee = new EmployeeDto(newId, "test name", "testjob", 10000,	LocalDateTime.of(1990, 01, 12, 8, 00));
	}

	@Test
	void testThatCreatedEmployeeIsListed() {

		createValid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1)).usingRecursiveComparison().isEqualTo(newEmployee);
	}

	@Test
	void testThatCreatedEmployeeWithoutNameIsNotListed() {

		EmployeeDto incorrectEmployee = new EmployeeDto(newId, null, "testjob", 10000, LocalDateTime.of(1990, 01, 12, 8, 00));

		createInvalid(incorrectEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size());
		assertThat(employeesAfter).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
	}

	@Test
	void testThatCorrectlyUpdatedEmployeeIsListed() {
		
		createValid(newEmployee);

		EmployeeDto updatedEmployee = new EmployeeDto(newId, "test name2", "testjob2", 20000,
				LocalDateTime.of(2000, 01, 12, 8, 00));

		updateEmployeeWithValid(updatedEmployee);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);

		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1)).usingRecursiveComparison().isEqualTo(updatedEmployee);
	}
	
	@Test
	void testThatUpdateEmployeeWithoutNameIsNotListed() {

		createValid(newEmployee);

		EmployeeDto updatedEmployee = new EmployeeDto(newId, null, "testjob2", 20000,
				LocalDateTime.of(2000, 01, 12, 8, 00));

		updateEmployeeWithInvalid(updatedEmployee);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);

		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1)).usingRecursiveComparison().isEqualTo(newEmployee);
	}

	private void updateEmployeeWithValid(EmployeeDto employeeDto) {
		webTestClient.put().uri(API_EMPLOYEES + '/' + employeeDto.getId()).bodyValue(employeeDto).exchange()
				.expectStatus().isOk();
	}

	private void updateEmployeeWithInvalid(EmployeeDto employeeDto) {
		webTestClient.put().uri(API_EMPLOYEES + '/' + employeeDto.getId()).bodyValue(employeeDto).exchange()
				.expectStatus().isBadRequest();
	}

	private void createValid(EmployeeDto newEmployee) {
		webTestClient.post().uri(API_EMPLOYEES).bodyValue(newEmployee).exchange().expectStatus().isOk();
	}

	private void createInvalid(EmployeeDto newEmployee) {
		webTestClient.post().uri(API_EMPLOYEES).bodyValue(newEmployee).exchange().expectStatus().isBadRequest();
	}

	private List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> allEmployees = webTestClient.get().uri(API_EMPLOYEES).exchange().expectStatus().isOk()
				.expectBodyList(EmployeeDto.class).returnResult().getResponseBody();

		Collections.sort(allEmployees, Comparator.comparing(e -> e.getId()));

		return allEmployees;
	}
}
