package hu.cubix.spring.hr.gaborh;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.CompanyForm;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.Qualification;
import hu.cubix.spring.hr.gaborh.service.CompanyService;
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

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<Position> positions = Arrays.asList(new Position("software developer", Qualification.NONE, 20000),
				new Position("data-analyst", Qualification.COLLEGE, 20000),
				new Position("devops", Qualification.NONE, 25000),
				new Position("lead software developer", Qualification.UNIVERSITY, 40000));

		List<CompanyForm> companyForms = Arrays.asList(new CompanyForm("LLC"), new CompanyForm("Limited Partnership"),
				new CompanyForm("Corporation"));

		List<Company> testCompanyList = Arrays.asList(
				new Company(01L, 1, "SmallCompany", "1100 Budapest, Vágóhíd u. 12.", companyForms.get(0), null),
				new Company(02L, 2, "BigCompany", "3000 Miskolc, Sajó u. 1.", companyForms.get(1), null),
				new Company(03L, 3, "HugeCompany", "8000 Zalaegerszeg, Komáromi út 30.", companyForms.get(2), null),
				new Company(04L, 4, "FirstCompany", "1205 Budapest, Bécsi út 3.", companyForms.get(2), null));

		List<Employee> testEmployeeList = Arrays.asList(
				new Employee("Ms Jane Doe", positions.get(0), 40000, LocalDateTime.now().minusYears(13),
						testCompanyList.get(0)),
				new Employee("Mr Jack Litle", positions.get(1), 30000, LocalDateTime.now().minusYears(5),
						testCompanyList.get(0)),
				new Employee("Adam Doe", positions.get(2), 25000, LocalDateTime.now().minusYears(2),
						testCompanyList.get(3)),
				new Employee("Mrs Lil Doe", positions.get(3), 40000, LocalDateTime.now().minusMonths(30),
						testCompanyList.get(0)),
				new Employee("Mr. Peter Sramek", positions.get(2), 35000, LocalDateTime.now().minusMonths(30),
						testCompanyList.get(0))
				);

		testEmployeeList.stream().forEach(employee -> {
			salaryService.setNewSalaryForAn(employee);
			System.out.println(employee.getSalary());
		});

		initDbService.clearDB();
		initDbService.insertTestData(positions, companyForms, testCompanyList, testEmployeeList);
		System.out.println(companyService.findAll(0, 2, "name", "desc").getContent());
	}

}
