package hu.cubix.spring.hr.gaborh.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
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
import hu.cubix.spring.hr.gaborh.service.EmployeeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class HrEmployeesRestController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeMapper employeeMapper;

//	@Autowired
//	private EmployeeRepository employeeRepository;		//az employee repository-t ide bekötni élesben nem jó megoldás, elvileg ide csak a service-n keresztül szabadna

// megoldás opcionális paraméterrel	
	@GetMapping
	public List<EmployeeDto> findAll(@RequestParam Optional<Integer> limitSalary, @SortDefault("id") Pageable pageable /*query param: page, size, sort*/) {
		// public List<EmployeeDto> findAll(@RequestParam(required = false) Integer
		// limitSalary) {
		List<Employee> employees = null;
		if (limitSalary.isPresent()) {
			Page<Employee> employeePage = employeeService.findBySalaryGreaterThan(limitSalary.get(), pageable);
			employees = employeePage.getContent();
			System.out.println(employeePage.getTotalElements());
			System.out.println(employeePage.isFirst());
			System.out.println(employeePage.isLast());
			System.out.println(employeePage.hasPrevious());
			System.out.println(employeePage.hasNext());
		} else {
			employees = employeeService.findAll();
		}

		return employeeMapper.employeesToDtos(employees);
	}

//	1. megoldás külön metódus streammel	
//	@GetMapping(params = "limit")
//	public List<EmployeeDto> listEmployeesWhoseSalaryIsMoreThan(@RequestParam long limit) {
//
//		List<Employee> allEmployees = employeeService.findAll();
//		return employeeMapper.employeesToDtos(allEmployees.stream().filter(e -> limit < e.getSalary()).toList());
//	}

//	@GetMapping("/{id}")
//	public EmployeeDto findById(@PathVariable long id) {
//		Employee employee = employeeService.findById(id);
//		if (employee == null) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//		}
//		return employeeMapper.employeeToDto(employee);
//	}

	@GetMapping("/{id}")
	public EmployeeDto findById(@PathVariable long id) {
		Employee employee = findByIdOrThrow(id); // throwing exception is outsourced to standalone method
		return employeeMapper.employeeToDto(employee);
	}

	private Employee findByIdOrThrow(long id) {
		return employeeService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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

	@GetMapping(params = "job")
	public List<EmployeeDto> listEmployeesWhoseJobIs(@RequestParam String job) {
		List<Employee> employeesWithGivenJob = employeeService.findByJob(job);
		return employeeMapper.employeesToDtos(employeesWithGivenJob);
	}

	@GetMapping(params = "prefix")
	public List<EmployeeDto> listEmployeesWithGiven(@RequestParam String prefix) {
		List<Employee> employeesWithGivenPrefix = employeeService.findByNamePrefix(prefix);
		return employeeMapper.employeesToDtos(employeesWithGivenPrefix);
	}

	@GetMapping(params = { "startdate", "enddate" })
	public List<EmployeeDto> findByStartDateBetween(@RequestParam LocalDateTime startdate,
			@RequestParam LocalDateTime enddate) {
		List<Employee> employeesStartedBetween = employeeService.findByStartDateBetween(startdate, enddate);
		return employeeMapper.employeesToDtos(employeesStartedBetween);
	}
}
