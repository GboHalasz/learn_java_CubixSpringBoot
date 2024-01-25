package hu.cubix.spring.hr.gaborh.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.dto.PositionDto;
import hu.cubix.spring.hr.gaborh.model.Qualification;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HrEmployeesRestControllerIT {

	private static final String API_EMPLOYEES = "/api/employees";

	@Autowired
	WebTestClient webTestClient;

	private List<EmployeeDto> employeesBefore;
	private long newId = 100L;
	private EmployeeDto newEmployee;
	private EmployeeDto updatedEmployee;

	@BeforeEach
	void setUp() {
		employeesBefore = getAllEmployees();
		if (employeesBefore.size() > 0) {
			newId = employeesBefore.get((employeesBefore.size() - 1)).getId() + 1;
		}

		PositionDto developer = new PositionDto("software developer", Qualification.NONE);
		newEmployee = new EmployeeDto(newId, "test name", developer, 10000, LocalDateTime.of(1990, 01, 12, 8, 00), null);
		updatedEmployee = new EmployeeDto(newId, "test name2", developer, 20000, LocalDateTime.of(2000, 01, 12, 8, 00), null);
	}

	@Test
	void testThatCreatedEmployeeIsListed() {

		createValid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);
		assertThat(employeesAfter.subList(0, employeesBefore.size()))
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1))
			.usingRecursiveComparison()
			.ignoringFields("id", "job.id")
			.isEqualTo(newEmployee);
	}

	@Test
	void testThatCreatedEmployeeWithoutNameIsNotListed() {
		newEmployee.setName(null);

		createInvalid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterCreateInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatCreatedEmployeeWithEmptyStringNameIsNotListed() {
		newEmployee.setName("");

		createInvalid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterCreateInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatCreatedEmployeeWithoutJobIsNotListed() {
		newEmployee.setJob(null);

		createInvalid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterCreateInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatCreatedEmployeeWith0SalaryIsNotListed() {
		newEmployee.setSalary(0);

		createInvalid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterCreateInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatCreatedEmployeeWithNegativeSalaryIsNotListed() {
		newEmployee.setSalary(-10000);

		createInvalid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterCreateInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatCreatedEmployeeWithFutureStartDateIsNotListed() {
		newEmployee.setStartDate(LocalDateTime.now().plusDays(1));

		createInvalid(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterCreateInvalidEmployee(employeesAfter);
	}

// Updates .........................................................................	

	@Test
	void testThatCorrectlyUpdatedEmployeeIsListed() {

		createValid(newEmployee);
		updateEmployeeWithValid(updatedEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);
		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1))
			.usingRecursiveComparison()
			.ignoringFields("job.id")
			.isEqualTo(updatedEmployee);
	}

	@Test
	void testThatUpdateEmployeeWithoutNameIsNotListed() {
		updatedEmployee.setName(null);

		createValid(newEmployee);
		updateEmployeeWithInvalid(updatedEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterUpdateWithInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatUpdateEmployeeWithEmptyStringNameIsNotListed() {
		updatedEmployee.setName("");

		createValid(newEmployee);
		updateEmployeeWithInvalid(updatedEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterUpdateWithInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatUpdateEmployeeWithoutJobIsNotListed() {
		updatedEmployee.setJob(null);

		createValid(newEmployee);
		updateEmployeeWithInvalid(updatedEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterUpdateWithInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatUpdateEmployeeWith0SalaryIsNotListed() {
		updatedEmployee.setSalary(0);

		createValid(newEmployee);
		updateEmployeeWithInvalid(updatedEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterUpdateWithInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatUpdateEmployeeWithNegativeSalaryIsNotListed() {
		updatedEmployee.setSalary(-1);

		createValid(newEmployee);
		updateEmployeeWithInvalid(updatedEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterUpdateWithInvalidEmployee(employeesAfter);
	}

	@Test
	void testThatUpdateEmployeeWithFutureStartDateIsNotListed() {
		updatedEmployee.setStartDate(LocalDateTime.now().plusDays(1));

		createValid(newEmployee);
		updateEmployeeWithInvalid(updatedEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertionsAfterUpdateWithInvalidEmployee(employeesAfter);
	}

	private void assertionsAfterCreateInvalidEmployee(List<EmployeeDto> employeesAfter) {
		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size());
		assertThat(employeesAfter).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
	}

	private void assertionsAfterUpdateWithInvalidEmployee(List<EmployeeDto> employeesAfter) {
		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);
		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1))
			.usingRecursiveComparison()
			.ignoringFields("job.id")
			.isEqualTo(newEmployee);
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

//lehet így is: 
//		Collections.sort(allEmployees, Comparator.comparing(EmployeeDto::getId));

		return allEmployees;
	}
}
