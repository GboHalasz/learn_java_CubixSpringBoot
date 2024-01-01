package hu.cubix.spring.hr.gaborh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.dto.AvsPJDto;
import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.CompanyForm;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.repository.CompanyFormRepository;
import hu.cubix.spring.hr.gaborh.repository.CompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionRepository;
import jakarta.transaction.Transactional;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyFormRepository companyFormRepository;
	
	@Autowired
	private PositionRepository positionRepository;

	@Transactional
	public Company create(Company company) {
		if (findById(company.getId()) != null) {
			return null;
		}
		return save(company);
	}

	@Transactional
	public Company update(Company company) {
		if (findById(company.getId()) == null) {
			return null;
		}
		return save(company);
	}

	public Company save(Company company) {
		CompanyForm companyForm = companyFormRepository.findByname(company.getCompanyForm().getName());		
		if (companyForm == null) {
			return null;
		}
		company.setCompanyForm(companyForm);
		return companyRepository.save(company);
	}

	public Page<Company> findAll(int pageNr, int pageSize, String sortBy, String sortDirection) {
		Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
		Pageable pageable = PageRequest.of(pageNr, pageSize, sort);
		return companyRepository.findAll(pageable);
	}

	public Company findById(long id) {
		return companyRepository.findById(id).orElse(null);
	}

	@Transactional
	public void delete(long cId) {
		Company company = companyRepository.findById(cId).get();
		for (Employee emp : company.getEmployees()) {
			emp.setCompany(null);
		}
		companyRepository.deleteById(cId);
	}

	public List<Company> findByEmployeesNotEmptyAndEmployeeSalaryGreaterThan(long minSalary) {
		return companyRepository.findByEmployeesNotEmptyAndEmployeeSalaryGreaterThan(minSalary);
	}

	public List<Company> findByEmployeesSizeGreaterThan(long limit) {
		return companyRepository.findByEmployeesSizeGreaterThan(limit);
	};

	public List<AvsPJDto> averageSalaryOfEmployeesGrouppedByJobAt(long companyId) {
		return companyRepository.averageSalaryOfEmployeesGrouppedByJobAt(companyId);
	};

	// Employee lista műveletek

	public Company addEmployee(long cId, Employee employee) { 
		Company company = companyRepository.findById(cId).get(); // a repository alapból Optional típust ad vissza, ezért a .get()
		Position position = positionRepository.findBynameOfPosition(employee.getJob().getNameOfPosition());		
		if (position == null) {
			return null;
		}
		employee.setJob(position);
				
		company.addEmployee(employee);
		employeeRepository.save(employee);
		return company;
	}

	public Company deleteEmployee(long id, long employeeId) {
		Company company = companyRepository.findById(id).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeRepository.save(employee);
		return company;
	}

	public Company replaceEmployees(long cId, List<Employee> employees) {
		Company company = companyRepository.findById(cId).get();
		for (Employee emp : company.getEmployees()) {
			emp.setCompany(null);
		}
		company.getEmployees().clear();
		for (Employee emp : employees) {
			Position position = positionRepository.findBynameOfPosition(emp.getJob().getNameOfPosition());		
			if (position == null) {
				return null;
			}
			emp.setJob(position);
			company.addEmployee(emp);
			employeeRepository.save(emp);
		}
		return company;
	}
	

	// CompanyForm műveletek

	@Transactional
	public CompanyForm create(CompanyForm companyForm) {
		if (findById(companyForm.getId()) != null) {
			return null;
		}
		return save(companyForm);
	}

	@Transactional
	public CompanyForm update(CompanyForm companyForm) {
		if (findCompanyFormById(companyForm.getId()) == null) {
			return null;
		}
		return save(companyForm);
	}

	public CompanyForm save(CompanyForm companyForm) {
		return companyFormRepository.save(companyForm);
	}

	public List<CompanyForm> findAllCompanyForms() {
		return companyFormRepository.findAll();
	}

	public CompanyForm findCompanyFormById(long id) {
		return companyFormRepository.findById(id).orElse(null);
	}

	@Transactional
	public void deleteCompanyForm(long id) {
		companyFormRepository.deleteById(id);
	}

}
