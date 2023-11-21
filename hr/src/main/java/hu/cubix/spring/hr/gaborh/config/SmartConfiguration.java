package hu.cubix.spring.hr.gaborh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.cubix.spring.hr.gaborh.service.EmployeeService;
import hu.cubix.spring.hr.gaborh.service.SmartEmployeeService;

@Configuration
@Profile("smart")
public class SmartConfiguration {

	@Bean
	public EmployeeService employeeService() {
		return new SmartEmployeeService();
	}
}
