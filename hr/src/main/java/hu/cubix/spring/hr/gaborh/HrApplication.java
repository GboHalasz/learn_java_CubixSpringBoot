package hu.cubix.spring.hr.gaborh;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.CompanyForm;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.PositionDetailsByCompany;
import hu.cubix.spring.hr.gaborh.model.Qualification;
import hu.cubix.spring.hr.gaborh.service.CompanyService;
import hu.cubix.spring.hr.gaborh.service.EmployeeService;
import hu.cubix.spring.hr.gaborh.service.InitDbService;
import hu.cubix.spring.hr.gaborh.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;

	@Autowired
	InitDbService initDbService;

	@Autowired
	CompanyService companyService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<Position> positions = Arrays.asList(new Position("software developer", Qualification.NONE),
				new Position("data-analyst", Qualification.COLLEGE), new Position("devops", Qualification.NONE),
				new Position("lead software developer", Qualification.UNIVERSITY));

		List<CompanyForm> companyForms = Arrays.asList(new CompanyForm("LLC"), new CompanyForm("Limited Partnership"),
				new CompanyForm("Corporation"));

		List<Company> testCompanyList = Arrays.asList(
				new Company(01L, 1, "SmallCompany", "1100 Budapest, Vágóhíd u. 12.", companyForms.get(0), null),
				new Company(02L, 2, "BigCompany", "3000 Miskolc, Sajó u. 1.", companyForms.get(1), null),
				new Company(03L, 3, "HugeCompany", "8000 Zalaegerszeg, Komáromi út 30.", companyForms.get(2), null),
				new Company(04L, 4, "FirstCompany", "1205 Budapest, Bécsi út 3.", companyForms.get(2), null));

		List<Employee> testEmployeeList = Arrays.asList(
				new Employee("Boss Doe", positions.get(0), 40000, LocalDateTime.now().minusYears(13),
						testCompanyList.get(0), null),
				new Employee("Junior Litle", positions.get(1), 30000, LocalDateTime.now().minusYears(5),
						testCompanyList.get(0), null),
				new Employee("Adam Doe", positions.get(2), 30000, LocalDateTime.now().minusYears(2),
						testCompanyList.get(3), null),
				new Employee("Little Joe", positions.get(0), 25000, LocalDateTime.now().minusYears(2),
						testCompanyList.get(3), null),
				new Employee("Mrs Lil Doe", positions.get(3), 40000, LocalDateTime.now().minusMonths(30),
						testCompanyList.get(0), null),
				new Employee("Mr. Peter Sramek", positions.get(2), 35000, LocalDateTime.now().minusMonths(30),
						testCompanyList.get(0), null));

		testEmployeeList.get(0).setUsername("user1");
		testEmployeeList.get(0).setPassword(passwordEncoder.encode("pass"));
		testEmployeeList.get(1).setUsername("user2");
		testEmployeeList.get(1).setPassword(passwordEncoder.encode("pass"));

		PositionDetailsByCompany pd = new PositionDetailsByCompany();
		pd.setCompany(testCompanyList.get(0));
		pd.setMinSalary(25000);
		pd.setPosition(positions.get(0));

		PositionDetailsByCompany pd2 = new PositionDetailsByCompany();
		pd2.setCompany(testCompanyList.get(0));
		pd2.setMinSalary(20000);
		pd2.setPosition(positions.get(1));

		PositionDetailsByCompany pd3 = new PositionDetailsByCompany();
		pd3.setCompany(testCompanyList.get(0));
		pd3.setMinSalary(30000);
		pd3.setPosition(positions.get(2));

		List<PositionDetailsByCompany> testPositionDetalisByCompanyList = Arrays.asList(pd, pd2, pd3);

		testEmployeeList.stream().forEach(employee -> {
			salaryService.setNewSalaryForAn(employee);
			System.out.println(employee.getSalary());
		});

		initDbService.clearDB();
		List<Employee> savedEmployees = initDbService.insertTestData(positions, companyForms, testCompanyList,
				testEmployeeList, testPositionDetalisByCompanyList);
		savedEmployees.get(1).setManager(savedEmployees.get(0));
		savedEmployees.get(0).setManager(savedEmployees.get(2));
		employeeService.update(savedEmployees.get(1));
		employeeService.update(savedEmployees.get(0));
	}

}
