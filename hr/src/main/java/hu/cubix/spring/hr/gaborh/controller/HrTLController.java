package hu.cubix.spring.hr.gaborh.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hu.cubix.spring.hr.gaborh.model.EmployeeTL;

@Controller
public class HrTLController {

	private Map<Long, EmployeeTL> employees = new HashMap<>();

	@GetMapping("/employees")
	public String home(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new EmployeeTL());
		return "employees";
	}

	@PostMapping("/employees")
	public String createEmployee(EmployeeTL employee) {
		employees.put(employee.getId(), employee);
		return "redirect:/employees";
	}

	@GetMapping("/employees/{id}")
	public String editEmployee(Map<String, Object> model, @PathVariable long id) {
		model.put("employee", employees.get(id));		
//		model.put("employee", allEmployees.stream().filter(e -> e.getEmployeeId() == id).findFirst().get());		//az oktatói megoldásban Lista van, abból szűrjük ki
		return "editEmployee";
	}

	@PostMapping("/updateEmployee")
	public String updateEmployee(EmployeeTL employee) {		
		employees.put(employee.getId(), employee);
//		for(int i=0; i< allEmployees.size(); i++) {											//ha allEmployees listánk lenne
//			if(allEmployees.get(i).getEmployeeId() == employee.getEmployeeId()) {
//				allEmployees.set(i, employee);
//				break;
//			}
		return "redirect:/employees";
	}

	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(@PathVariable long id) {
		employees.remove(id);
//		allEmployees.removeIf(e -> e.getEmployeeId() == id );			//az oktatói megoldásban allEmployees Lista van, amiből id alapján a removeIf metódussal legegyszerűbb törölni
		return "redirect:/employees";									// a / kell, hogy ne relatíven irányítson át
		
	}

}
