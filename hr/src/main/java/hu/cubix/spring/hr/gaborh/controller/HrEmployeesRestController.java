package hu.cubix.spring.hr.gaborh.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;

@RestController
@RequestMapping("/api/employees")
public class HrEmployeesRestController {

	private Map<Long, EmployeeDto> employees = new HashMap<>();

	{
		employees.put(1L, new EmployeeDto(1L, "developer", 10000, LocalDateTime.of(1990, 01, 12, 8, 00)));
		employees.put(2L, new EmployeeDto(2L, "developer", 20000, LocalDateTime.of(1990, 01, 12, 8, 00)));
		employees.put(3L, new EmployeeDto(3L, "developer", 30000, LocalDateTime.of(1990, 01, 12, 8, 00)));
	}

	@GetMapping
	public List<EmployeeDto> listAll() {
		return new ArrayList<>(employees.values());
	}

	@GetMapping(params = "limit")
	public List<EmployeeDto> listEmployeesWhoseSalaryIsMoreThan(@RequestParam long limit) {

//		List<EmployeeDto> result = new ArrayList<>();

//		for (EmployeeDto employee : employees.values()) {
//			if (limit < employee.getSalary()) {
//				result.add(employee);
//			}
//		}
//		return result;

//	1. megoldás külön metódus streammel

		return employees.values().stream().filter(e -> limit < e.getSalary()).toList();
	}

// 2. megoldás egy metódusban opcionális paraméterrel

//	@GetMapping
//	public List<EmployeeDto> listAll(@RequestParam Optional<Integer> limit) {

//	//vagy public List<EmployeeDto> listAll(@RequestParam(required = false) Integer limit) {		 //ebben az esetben limit == null vizsgálatot csinálunk nem isEmpty-t!!  Fontos, hogy a típus Integer, mert az == null nem értelmezhető a primitív int típuson!!       

//		return limit.isEmpty() ? new ArrayList<>(employees.values())
//				: employees.values().stream().filter(e -> e.getSalary() > limit).toList();
//	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> findById(@PathVariable long id) {
		EmployeeDto employeeDto = employees.get(id);
		if (employeeDto == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(employeeDto);
	}

	@PostMapping
	public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employee) {
		if (employees.containsKey(employee.getId()))
			return ResponseEntity.badRequest().build();

		employees.put(employee.getId(), employee);
		return ResponseEntity.ok(employee);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> update(@PathVariable long id, @RequestBody EmployeeDto employee) {
		employee.setId(id);
		if (!employees.containsKey(id))
			return ResponseEntity.notFound().build();

		employees.put(id, employee);
		return ResponseEntity.ok(employee);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		employees.remove(id);
	}
}
