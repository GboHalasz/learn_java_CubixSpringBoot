package hu.cubix.spring.hr.gaborh.controller;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import hu.cubix.spring.hr.gaborh.dto.CompanyDto;
import hu.cubix.spring.hr.gaborh.dto.EmployeeDto;
import hu.cubix.spring.hr.gaborh.serializers.CompanyDtoSerializerExcludeWorkers;

@RestController
@RequestMapping("/api/companies")
public class HrCompaniesRestController {

	private Map<String, CompanyDto> companies = new HashMap<>();

	{
		companies.put("c01", new CompanyDto("c01", 1L, "BigCompany", "1100 Budapest, Cél utca 6."));
		companies.put("c02", new CompanyDto("c02", 2L, "MediumCompany", "3100 Miskolc, Eldugott utca 120."));
		companies.put("c03", new CompanyDto("c03", 3L, "SmallCompany", "1100 Zalaegerszeg, Bécsi út 1."));
	}

	@GetMapping
	public String listAll() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(CompanyDto.class, new CompanyDtoSerializerExcludeWorkers());
		mapper.registerModule(module);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ArrayList<>(companies.values()));
	}

	@GetMapping(params = "full=true")
	public List<CompanyDto> listAllexceptWorkers() {
		return new ArrayList<>(companies.values());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> findById(@PathVariable String id, @RequestParam(required = false) Boolean full) {
		CompanyDto companyDto = companies.get(id);
		if (companyDto == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(companyDto);
	}

	@PostMapping
	public ResponseEntity<CompanyDto> create(@RequestBody CompanyDto company) {
		if (companies.containsKey(company.getId()))
			return ResponseEntity.badRequest().build();

		companies.put(company.getId(), company);
		return ResponseEntity.ok(company);
	}

	@PostMapping("/{cId}/addemployee")
	public ResponseEntity<CompanyDto> addEmployee(@PathVariable String cId, @RequestBody EmployeeDto employee) {
		CompanyDto company = companies.get(cId);
		if (!companies.containsKey(cId))
			return ResponseEntity.badRequest().build();

		company.getWorkers().put(employee.getId(), employee);
		return ResponseEntity.ok(company);
	}

	@PostMapping("/{cId}/replaceworkers")
	public ResponseEntity<CompanyDto> replaceWorkers(@PathVariable String cId,
			@RequestBody Map<Long, EmployeeDto> employeeList) {
		CompanyDto company = companies.get(cId);
		if (!companies.containsKey(cId))
			return ResponseEntity.badRequest().build();
		company.setWorkers(employeeList);
		return ResponseEntity.ok(company);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> update(@RequestBody CompanyDto company, @PathVariable String id) {
		company.setId(id);
		if (!companies.containsKey(id))
			return ResponseEntity.notFound().build();

		companies.put(id, company);
		return ResponseEntity.ok(company);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		companies.remove(id);
	}

	@DeleteMapping("/{cId}/del/{eId}")
	public void deleteWorker(@PathVariable String cId, @PathVariable Long eId) {
		CompanyDto company = companies.get(cId);
		company.getWorkers().remove(eId);
	}

}
