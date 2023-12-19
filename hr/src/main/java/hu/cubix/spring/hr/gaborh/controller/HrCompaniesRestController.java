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

import com.fasterxml.jackson.annotation.JsonView;

import hu.cubix.spring.hr.gaborh.dto.CompanyDto;
import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.dto.Views;
import hu.cubix.spring.hr.gaborh.mapper.CompanyMapper;
import hu.cubix.spring.hr.gaborh.mapper.EmployeeMapper;
import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.service.CompanyService;
import hu.cubix.spring.hr.gaborh.service.EmployeeSuperService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
public class HrCompaniesRestController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EmployeeSuperService employeeService;

	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private EmployeeMapper employeeMapper;

	// 1. megoldás a full paraméter kezelésére
//	@GetMapping
//	public List<CompanyDto> findAll(@RequestParam Optional<Boolean> full) {
//		if(full.orElse(false)) {		
//			return new ArrayList<>(companies.values());
//		} else {
//			return companies.values().stream()
//				.map(this::mapWithoutEmployees)
//				.toList();
//		}
//	}

	// 2. megoldás full paraméter kezelésére
	@GetMapping
	@JsonView(Views.BaseData.class)
	public List<CompanyDto> findAllWithoutEmployees() {
		List<Company> allCompanies = companyService.findAll();
		return companyMapper.companiesToDtos(allCompanies);
	}

	@GetMapping(params = "full=true")
	public List<CompanyDto> findAll() {
		List<Company> allCompanies = companyService.findAll();
		return companyMapper.companiesToDtos(allCompanies);
	}

	private CompanyDto mapWithoutEmployees(CompanyDto c) {
		return new CompanyDto(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
	}

	@GetMapping("/{id}")
	public CompanyDto findById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
		Company company = getCompanyOrThrow(id);
		if (full.orElse(false)) {
			return companyMapper.companyToDto(company);
		} else {
			return mapWithoutEmployees(companyMapper.companyToDto(company));
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

	@PostMapping("/{cId}/employees")

	public EmployeeDto addEmployee(@PathVariable long cId, @RequestBody @Valid EmployeeDto employeeDto) {
		Company company = getCompanyOrThrow(cId);
		Employee employee = employeeService.findById(employeeDto.getId());

		if (employee != null) {
			employeeDto = employeeMapper.employeeToDto(employee);
			employeeDto.setCompany(company);
			employeeService.update(employeeMapper.dtoToEmployee(employeeDto));
			return (employeeDto);
		}

		employeeDto.setCompany(company);
		employeeService.create(employeeMapper.dtoToEmployee(employeeDto));
		return (employeeDto);
	}

	@PutMapping("/{cId}/employees")
	public CompanyDto replaceAllWorkers(@PathVariable long cId, @RequestBody List<@Valid EmployeeDto> employeeDtos) {
		Company company = getCompanyOrThrow(cId);
		if (company.getEmployees().size() > 0) {
			company.getEmployees().stream().forEach(e -> e.setCompany(null));
		}

		for (EmployeeDto employeeDto : employeeDtos) {
			employeeDto.setCompany(company);
			employeeService.create(employeeMapper.dtoToEmployee(employeeDto));
		}
		companyService.update(company);
		return (companyMapper.companyToDto(company));
	}

	@PutMapping("/{id}")
	public CompanyDto update(@RequestBody CompanyDto companyDto, @PathVariable long id) {
		companyDto.setId(id);

		Company company = companyMapper.dtoToCompany(companyDto);
		Company updatedCompany = companyService.update(company);

		if (updatedCompany == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		return companyMapper.companyToDto(updatedCompany);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		companyService.delete(id);
	}

	@DeleteMapping("/{cId}/employees/{eId}")
	public EmployeeDto deleteWorker(@PathVariable long cId, @PathVariable long eId) {
		getCompanyOrThrow(cId);
		Employee employee = getEmployeeOrThrow(eId);
		employee.setCompany(null);
		employeeService.update(employee);
		return (employeeMapper.employeeToDto(employee));
	}

	@Transactional
	private Employee getEmployeeOrThrow(long cId) {
		Employee employee = employeeService.findById(cId);
		if (employee == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return employee;
	}

	@Transactional
	private Company getCompanyOrThrow(long cId) {
		Company company = companyService.findById(cId);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return company;
	}

}
