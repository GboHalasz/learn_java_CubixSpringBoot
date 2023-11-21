package hu.cubix.spring.hr.gaborh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.cubix.spring.hr.gaborh.service.DefaultEmployeeService;
import hu.cubix.spring.hr.gaborh.service.EmployeeService;

@Configuration
@Profile("!smart")
public class DefaultConfiguration {

	@Bean
	public EmployeeService employeeService() {
		return new DefaultEmployeeService();
	}
}
