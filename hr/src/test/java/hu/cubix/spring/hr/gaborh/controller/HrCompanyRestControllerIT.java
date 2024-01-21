package hu.cubix.spring.hr.gaborh.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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
import hu.cubix.spring.hr.gaborh.mapper.PositionMapper;
import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.CompanyForm;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.Qualification;
import hu.cubix.spring.hr.gaborh.repository.CompanyFormRepository;
import hu.cubix.spring.hr.gaborh.repository.CompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionDetailsByCompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionRepository;
import hu.cubix.spring.hr.gaborh.service.CompanyService;
import hu.cubix.spring.hr.gaborh.service.EmployeeService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HrCompanyRestControllerIT {

	private static final String API_COMPANIES = "/api/companies";

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	CompanyService companyService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	CompanyFormRepository companyFormRepository;

	@Autowired
	PositionRepository positionRepository;

	@Autowired
	PositionMapper positionMapper;

	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

	private Company savedCompany;
	private PositionDto positionDto;
	private CompanyForm savedCompanyForm;

	@BeforeEach
	public void init() {
		employeeRepository.deleteAllInBatch();
		positionDetailsByCompanyRepository.deleteAllInBatch();
		companyRepository.deleteAllInBatch();
		companyFormRepository.deleteAllInBatch();
		positionRepository.deleteAllInBatch();

		positionDto = new PositionDto("developer", Qualification.NONE);
		savedCompanyForm = companyFormRepository.save(new CompanyForm("LLC"));
		savedCompany = companyService
				.create(new Company(01L, 1L, "SmallCompany", "1100 Budapest, Vágóhíd u. 12.", savedCompanyForm, null));
	}

	@Test
	void testThatAddedEmployeeIsListedAtCompany() {
		// ARRANGE
		List<Employee> employeesBefore = companyService.findByIdWithEmployees(savedCompany.getId()).getEmployees();

		EmployeeDto testEmployee = new EmployeeDto("test name", positionDto, 10000,
				LocalDateTime.of(1990, 01, 12, 8, 00));

		// ACT
		webTestClient.post().uri(API_COMPANIES + "/" + savedCompany.getId() + "/employees").bodyValue(testEmployee)
				.exchange().expectStatus().isOk();

		List<Employee> employeesAfter = companyService.findByIdWithEmployees(savedCompany.getId()).getEmployees();
		Employee addedEmployee = employeesAfter.get(employeesBefore.size());

		// ASSERT
		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);
		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(addedEmployee.getName()).isEqualTo(testEmployee.getName());
		assertThat(addedEmployee.getSalary()).isEqualTo(testEmployee.getSalary());
		assertThat(addedEmployee.getJob().getNameOfPosition()).isEqualTo(testEmployee.getJob().getNameOfPosition());
		assertThat(addedEmployee.getStartDate()).isEqualTo(testEmployee.getStartDate());

	}

	@Test
	void testThatDeletedEmployeeIsNotListedAtCompany() {
		// ARRANGE
		List<Employee> employeesBefore = companyService.findByIdWithEmployees(savedCompany.getId()).getEmployees();

		EmployeeDto testEmployee = new EmployeeDto("test name", positionDto, 10000,
				LocalDateTime.of(1990, 01, 12, 8, 00));

		webTestClient.post().uri(API_COMPANIES + "/" + savedCompany.getId() + "/employees").bodyValue(testEmployee)
				.exchange().expectStatus().isOk();

		List<Employee> employeesAfterAddOne = companyService.findByIdWithEmployees(savedCompany.getId()).getEmployees();

		Employee addedEmployee = employeesAfterAddOne.get(employeesAfterAddOne.size() - 1);

		// ACT
		webTestClient.delete().uri(API_COMPANIES + "/" + savedCompany.getId() + "/employees/" + addedEmployee.getId())
				.exchange().expectStatus().isOk();

		List<Employee> employeesAfterDelete = companyService.findByIdWithEmployees(savedCompany.getId()).getEmployees();

		// ASSERT
		assertThat(employeesAfterAddOne.size()).isEqualTo(employeesBefore.size() + 1);
		assertThat(employeesAfterDelete.size()).isEqualTo(employeesBefore.size());
		assertThat(employeesAfterDelete).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
	}

	@Test
	void testThatReplacedEmployeesAreNotListedAndAddedEmployeesAreListedAtCompany() {
		// ARRANGE

		Position savedPosition = positionRepository.save(positionMapper.dtoToPosition(positionDto));
		Employee savedEmployee1 = employeeService.create(
				new Employee("test name1", savedPosition, 10000, LocalDateTime.of(1990, 01, 12, 8, 00), savedCompany));
		Employee savedEmployee2 = employeeService.create(
				new Employee("test name2", savedPosition, 20000, LocalDateTime.of(1995, 01, 12, 8, 00), savedCompany));

		List<Employee> employeesBefore = companyService.findByIdWithEmployees(savedCompany.getId()).getEmployees();

		List<Employee> testEmployeesList = Arrays.asList(
				new Employee("Ms Jane Doe", savedPosition, 40000, LocalDateTime.now().minusYears(13), null),
				new Employee("Mr Jack Litle", savedPosition, 30000, LocalDateTime.now().minusYears(5), null),
				new Employee("Adam Doe", savedPosition, 30000, LocalDateTime.now().minusYears(2), null));

		// ACT
		webTestClient.put().uri(API_COMPANIES + "/" + savedCompany.getId() + "/employees").bodyValue(testEmployeesList)
				.exchange().expectStatus().isOk();

		List<Employee> employeesAfter = companyService.findByIdWithEmployees(savedCompany.getId()).getEmployees();

		// ASSERT
		assertThat(employeesAfter.size()).isEqualTo(testEmployeesList.size());
		assertThat(employeesBefore).contains(savedEmployee1);
		assertThat(employeesBefore).contains(savedEmployee2);
		assertThat(employeesAfter).doesNotContain(savedEmployee1);
		assertThat(employeesAfter).doesNotContain(savedEmployee2);
		for (int i = 0; i < employeesAfter.size(); i++) {
			assertThat(employeesAfter.get(i).getName()).isEqualTo(testEmployeesList.get(i).getName());
			assertThat(employeesAfter.get(i).getJob()).isEqualTo(testEmployeesList.get(i).getJob());
			assertThat(employeesAfter.get(i).getSalary()).isEqualTo(testEmployeesList.get(i).getSalary());
			assertThat(employeesAfter.get(i).getStartDate()).isCloseTo(testEmployeesList.get(i).getStartDate(),
					within(1, ChronoUnit.MICROS));			
		}
	}
}
