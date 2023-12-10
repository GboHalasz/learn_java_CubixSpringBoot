package hu.cubix.spring.hr.gaborh.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;

import hu.cubix.spring.hr.gaborh.dto.CompanyDto;
import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.dto.Views;

@RestController
@RequestMapping("/api/companies")
public class HrCompaniesRestController {

	private Map<Long, CompanyDto> companies = new HashMap<>();

	{
		companies.put(1L, new CompanyDto(1L, 1L, "BigCompany", "1100 Budapest, Cél utca 6.", new HashMap<>() ) );
		companies.put(2L, new CompanyDto(2L, 2L, "MediumCompany", "3100 Miskolc, Eldugott utca 120.",  new HashMap<>()) );
		companies.put(3L, new CompanyDto(3L, 3L, "SmallCompany", "1100 Zalaegerszeg, Bécsi út 1.",  new HashMap<>()) );
	}

	//1. megoldás a full paraméter kezelésére
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

	//2. megoldás full paraméter kezelésére
		@GetMapping
		@JsonView(Views.BaseData.class)
		public List<CompanyDto> findAll() {
			return new ArrayList<>(companies.values());
		}
		
		@GetMapping(params = "full=true")
		public List<CompanyDto> findAllWithoutEmployees() {
			return new ArrayList<>(companies.values());
		}


		private CompanyDto mapWithoutEmployees(CompanyDto c) {
			return new CompanyDto(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
		}
		
		@GetMapping("/{id}")
		public CompanyDto findById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
			CompanyDto companyDto = getCompanyOrThrow(id);
			if(full.orElse(false)) {	
				return companyDto;
			} else {
				return mapWithoutEmployees(companyDto);
			}
		}

	@PostMapping
	public ResponseEntity<CompanyDto> create(@RequestBody CompanyDto company) {
		if (companies.containsKey(company.getId()))
			return ResponseEntity.badRequest().build();

		companies.put(company.getId(), company);
		return ResponseEntity.ok(company);
	}

	@PostMapping("/{cId}/employees")
	public CompanyDto addEmployee(@PathVariable long cId, @RequestBody EmployeeDto employeeDto) {
		CompanyDto companyDto = getCompanyOrThrow(cId);

		companyDto.getWorkers().put(employeeDto.getId(), employeeDto);
		return companyDto;
	}

	

	@PutMapping("/{cId}/employees")
	public CompanyDto replaceAllWorkers(@PathVariable long cId,	@RequestBody Map<Long, EmployeeDto> employeeDtos) {
		CompanyDto companyDto = getCompanyOrThrow(cId);
		
		companyDto.setWorkers(employeeDtos);
		return companyDto;
	}

	@PutMapping("/{id}")
	public CompanyDto update(@RequestBody CompanyDto company, @PathVariable Long id) {
		company.setId(id);
		getCompanyOrThrow(id);
		
		companies.put(id, company);
		return company;
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		companies.remove(id);
	}

	@DeleteMapping("/{cId}/employees/{eId}")
	public CompanyDto deleteWorker(@PathVariable Long cId, @PathVariable Long eId) {
		CompanyDto companyDto = getCompanyOrThrow(cId);
		companyDto.getWorkers().remove(eId);
//		companyDto.getEmployees().removeIf(e -> e.getId() == eId);						//oktatói megoldásban listában tárolja a dolgozókat	a CompanyDto-ban
		return companyDto;
	}
	
	private CompanyDto getCompanyOrThrow(Long cId) {
		CompanyDto companyDto = companies.get(cId);
		if (companyDto == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return companyDto;
	}

}
