package hu.cubix.spring.hr.gaborh.controller;

import java.util.List;
import java.util.Map;
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
import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.service.CompanyService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
public class HrCompaniesRestController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyMapper companyMapper;

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
		CompanyDto companyDto = getCompanyOrThrow(id);
		if (full.orElse(false)) {
			return companyDto;
		} else {
			return mapWithoutEmployees(companyDto);
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
	public CompanyDto addEmployee(@PathVariable long cId, @RequestBody @Valid EmployeeDto employeeDto) {
		CompanyDto companyDto = getCompanyOrThrow(cId);
		companyDto.getWorkers().put(employeeDto.getId(), employeeDto);
		Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
		return companyMapper.companyToDto(updatedCompany);
	}

	@PutMapping("/{cId}/employees")
	public CompanyDto replaceAllWorkers(@PathVariable long cId,
			@RequestBody Map<Long, @Valid EmployeeDto> employeeDtos) {
		CompanyDto companyDto = getCompanyOrThrow(cId);
		companyDto.setWorkers(employeeDtos);
		Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
		return companyMapper.companyToDto(updatedCompany);
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
	public CompanyDto deleteWorker(@PathVariable long cId, @PathVariable long eId) {
		CompanyDto companyDto = getCompanyOrThrow(cId);
		companyDto.getWorkers().remove(eId);
		Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
		return companyMapper.companyToDto(updatedCompany);
	}

	private CompanyDto getCompanyOrThrow(long cId) {
		Company company = companyService.findById(cId);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return companyMapper.companyToDto(company);
	}

}
