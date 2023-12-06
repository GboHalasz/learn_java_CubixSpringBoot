package hu.cubix.spring.hr.gaborh;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<Employee> employees = Arrays.asList(
				new Employee(01L, "Jane Doe", "software developer", 10000, LocalDateTime.of(1990, 01, 12, 8, 00)),
				new Employee(02L, "Jack Doe", "software developer", 10000, LocalDateTime.of(2017, 01, 12, 8, 00)),
				new Employee(03L, "Adam Doe", "software developer", 10000, LocalDateTime.of(2021, 01, 12, 8, 00)),
				new Employee(04L, "Lil Doe", "software developer", 10000, LocalDateTime.of(2022, 01, 12, 8, 00)));

		employees.stream().forEach(employee -> {
			salaryService.setNewSalaryForAn(employee);
			System.out.println(employee.getSalary());
		});

	}

}
