package hu.cubix.spring.hr.gaborh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.repository.CompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import jakarta.transaction.Transactional;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

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
		return companyRepository.save(company);
	}

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public Company findById(long id) {
		return companyRepository.findById(id).orElse(null);
	}

	@Transactional
	public void delete(long id) {
		companyRepository.deleteById(id);
	}

	// Employee lista műveletek

	public Company addEmployee(long id, Employee employee) {				//a repository alapból Optional típust ad vissza, ezért a .get()
		Company company = companyRepository.findById(id).get();
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

	public Company replaceEmployees(long id, List<Employee> employees) {
		Company company = companyRepository.findById(id).get();
		for (Employee emp : company.getEmployees()) {
			emp.setCompany(null);
		}
		company.getEmployees().clear();
		for (Employee emp : employees) {
			company.addEmployee(emp);
			employeeRepository.save(emp);
		}
		return company;
	}

}
