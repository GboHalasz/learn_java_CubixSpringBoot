package hu.cubix.spring.hr.gaborh.controller;

import java.util.List;
import java.util.Optional;

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

import hu.cubix.spring.hr.gaborh.dto.AvsPJDto;
import hu.cubix.spring.hr.gaborh.dto.CompanyDto;
import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.mapper.CompanyMapper;
import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.service.CompanyService;
import hu.cubix.spring.hr.gaborh.service.EmployeeService;
import hu.cubix.spring.hr.gaborh.service.SalaryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
public class HrCompaniesRestController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private SalaryService salaryService;

	// 1. megoldás a full paraméter kezelésére
	@GetMapping
	public List<CompanyDto> findAll(@RequestParam Optional<Boolean> full) {

		List<Company> companies = full.orElse(false) 
				? companyService.findAllWithEmployees()
				: companyService.findAll();

		return mapCompanies(companies, full);
	}

	@GetMapping("/{id}")
	public CompanyDto findById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
		Company company =  getCompanyOrThrow(id, full);
		if (full.orElse(false)) {
			return companyMapper.companyToDto(company);
		} else {
			return companyMapper.companyToSummaryDto(company);
		}
	}

	@PostMapping
	public CompanyDto create(@RequestBody CompanyDto companyDto) {

		Company company = companyMapper.dtoToCompany(companyDto);
		Company savedCompany = companyService.create(company);

		if (savedCompany == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return companyMapper.companyToDto(savedCompany);
	}

	@PutMapping("/{id}")
	public CompanyDto update(@RequestBody CompanyDto companyDto, @PathVariable long id) {
		companyDto.setId(id);
		Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));

		if (updatedCompany == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		return companyMapper.companyToDto(updatedCompany);
	}

	@PostMapping("/{cId}/employees")
	public CompanyDto addEmployee(@PathVariable long cId, @RequestBody @Valid EmployeeDto employeeDto) {

		Company company = companyService.addEmployee(cId, companyMapper.dtoToEmployee(employeeDto));
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return companyMapper.companyToDto(company);
	}

	@PutMapping("/{cId}/employees")
	public CompanyDto replaceAllEmployees(@PathVariable long cId, @RequestBody List<@Valid EmployeeDto> employeeDtos) {
		Company company = companyService.replaceEmployees(cId, companyMapper.dtosToEmployees(employeeDtos));
		return companyMapper.companyToDto(company);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		companyService.delete(id);
	}

	@DeleteMapping("/{cId}/employees/{eId}")
	public CompanyDto deleteWorker(@PathVariable long cId, @PathVariable long eId) {
		Company company = companyService.deleteEmployee(cId, eId);
		return companyMapper.companyToDto(company);
	}

	@Transactional
	private Employee getEmployeeOrThrow(long cId) {
		Optional<Employee> employee = employeeService.findById(cId);
		if (employee.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return employee.get();
	}

	@GetMapping(params = "minSalary")
	public List<CompanyDto> listCompaniesHaveAtLeastOneEmployeeWithSalaryMoreThan(@RequestParam long minSalary,
			@RequestParam Optional<Boolean> full) {

		List<Company> filteredCompanies = companyService.findByEmployeesNotEmptyAndEmployeeSalaryGreaterThan(minSalary);
		return mapCompanies(filteredCompanies, full);
	}

	@GetMapping(params = "employeesLimit")
	public List<CompanyDto> findByEmployeesSizeGreaterThan(@RequestParam long employeesLimit,
			@RequestParam Optional<Boolean> full) {

		List<Company> filteredCompanies = companyService.findByEmployeesSizeGreaterThan(employeesLimit);
		return mapCompanies(filteredCompanies, full);
	}

	@GetMapping("/{id}/salaryStats")
	public List<AvsPJDto> averageSalaryOfEmployeesGrouppedByJobAt(@PathVariable long id) {
		return companyService.findAverageSalariesByPosition(id);

	};

	@PutMapping("/{id}/raiseMin/{position}/{minSalary}")
	public void raiseMinSalary(@PathVariable long id, @PathVariable String position, @PathVariable int minSalary) {
		salaryService.raiseMinSalary(id, position, minSalary);
	}

	@Transactional
	private Company getCompanyOrThrow(long cId, Optional<Boolean> full) {
		Company company = full.orElse(false) 
				? companyService.findByIdWithEmployees(cId)
				: companyService.findById(cId);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return company;
	}

	private List<CompanyDto> mapCompanies(List<Company> companies, Optional<Boolean> full) {
		if (full.orElse(false)) {
			return companyMapper.companiesToDtos(companies);
		} else {
			return companyMapper.companiesToSummaryDtos(companies);
		}
	}

}
