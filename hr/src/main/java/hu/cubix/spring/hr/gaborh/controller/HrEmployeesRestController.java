package hu.cubix.spring.hr.gaborh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.mapper.EmployeeMapper;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.service.EmployeeSuperService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class HrEmployeesRestController {

	@Autowired
	private EmployeeSuperService employeeService;

	@Autowired
	private EmployeeMapper employeeMapper;

	@GetMapping
	public List<EmployeeDto> listAll() {
		List<Employee> allEmployees = employeeService.findAll();
		return employeeMapper.employeesToDtos(allEmployees);
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
		List<Employee> allEmployees = employeeService.findAll();
		return employeeMapper.employeesToDtos(allEmployees.stream().filter(e -> limit < e.getSalary()).toList());
	}

// 2. megoldás egy metódusban opcionális paraméterrel

//	@GetMapping
//	public List<EmployeeDto> listAll(@RequestParam Optional<Integer> limit) {

//	//vagy public List<EmployeeDto> listAll(@RequestParam(required = false) Integer limit) {		 //ebben az esetben limit == null vizsgálatot csinálunk nem isEmpty-t!!  Fontos, hogy a típus Integer, mert az == null nem értelmezhető a primitív int típuson!!       

//		return limit.isEmpty() ? new ArrayList<>(employees.values())
//				: employees.values().stream().filter(e -> e.getSalary() > limit).toList();
//	}

	@GetMapping("/{id}")
	public EmployeeDto findById(@PathVariable long id) {
		Employee employee = employeeService.findById(id);
		if (employee == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return employeeMapper.employeeToDto(employee);
	}

	@PostMapping
	public EmployeeDto create(@RequestBody @Valid EmployeeDto employeeDto) {

		Employee employee = employeeMapper.dtoToEmployee(employeeDto);
		Employee savedEmployee = employeeService.create(employee);

		if (savedEmployee == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return employeeMapper.employeeToDto(savedEmployee);
	}

	@PutMapping("/{id}")
	public EmployeeDto update(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {

		employeeDto.setId(id);
		Employee employee = employeeMapper.dtoToEmployee(employeeDto);
		Employee updatedEmployee = employeeService.update(employee);

		if (updatedEmployee == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		return employeeMapper.employeeToDto(updatedEmployee);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		employeeService.delete(id);
	}

	@PostMapping("/raisepercent")
	public int getPayRaisePercentOf(@RequestBody Employee employee) {

		return employeeService.getPayRaisePercent(employee);
	}
}
