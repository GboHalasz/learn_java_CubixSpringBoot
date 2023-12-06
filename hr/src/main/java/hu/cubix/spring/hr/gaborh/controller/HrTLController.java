package hu.cubix.spring.hr.gaborh.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hu.cubix.spring.hr.gaborh.model.Employee;

@Controller
public class HrTLController {

	private Map<Long, Employee> employees = new HashMap<>();

	@GetMapping("/employees")
	public String home(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new Employee());
		return "employees";
	}

	@PostMapping("/employees")
	public String createEmployee(Employee employee) {
		employees.put(employee.getId(), employee);
		return "redirect:/employees";
	}

	@GetMapping("/employees/{id}")
	public String editEmployee(Map<String, Object> model, @PathVariable long id) {
		model.put("employee", employees.get(id));
		model.put("newEmployee", new Employee());
		return "employee";
	}

	@PostMapping("/employees/{id}")
	public String updateEmployee(@PathVariable long id, Employee employee) {
		employee.setId(id);
		if (!employees.containsKey(id))
			return "redirect:/error";

		employees.put(id, employee);
		return "redirect:/employees";
	}

	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(@PathVariable long id) {
		employees.remove(id);
		return "redirect:/employees";
	}

}
