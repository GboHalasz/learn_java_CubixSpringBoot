package hu.cubix.spring.hr.gaborh.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hu.cubix.spring.hr.gaborh.model.Employee;

@Controller
public class HrTLController {

	private List<Employee> employees = new ArrayList<>();

	@GetMapping("/employees")
	public String home(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new Employee());
		return "index";
	}

	@PostMapping("/employees")
	public String createEmployee(Employee employee) {
		employees.add(employee);
		return "redirect:/employees";
	}
}
