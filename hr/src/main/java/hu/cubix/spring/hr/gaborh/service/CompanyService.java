package hu.cubix.spring.hr.gaborh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Transactional
	public Company save(Company company) {
		processCompanyForm(company);
		return companyRepository.save(company);
	}

	private void processCompanyForm(Company company) {
		CompanyForm companyForm = null;
		String formName = company.getCompanyForm().getName();
		if (formName != null) {
			companyForm = companyFormRepository.findByname(formName);
			if (companyForm == null) {
				companyForm = companyFormRepository.save(new CompanyForm(formName));
			}
		}
		company.setCompanyForm(companyForm);
	}

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public List<Company> findAllWithEmployees() {
		return companyRepository.findAllWithEmployees();
	}

	public Company findById(long id) {
		return companyRepository.findById(id).orElse(null);
	}

	public Company findByIdWithEmployees(long id) {
		return companyRepository.findByIdWithEmployees(id).orElse(null);
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

	public List<AvsPJDto> findAverageSalariesByPosition(long companyId) {
		return companyRepository.findAverageSalariesByPosition(companyId);
	};

	// Employee lista műveletek
	@Transactional
	public Company addEmployee(long cId, Employee employee) {
		Company company = companyRepository.findByIdWithEmployees(cId).get(); // a repository alapból Optional típust ad
																				// vissza, ezért a .get()

		processPosition(employee);
		company.addEmployee(employee);
		employeeRepository.save(employee);
		return company;
	}

	@Transactional
	public Company deleteEmployee(long id, long employeeId) {
		Company company = companyRepository.findByIdWithEmployees(id).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeRepository.save(employee);
		return company;
	}

	@Transactional
	public Company replaceEmployees(long cId, List<Employee> employees) {
		Company company = companyRepository.findByIdWithEmployees(cId).get();
		for (Employee emp : company.getEmployees()) {
			emp.setCompany(null);
		}
		company.getEmployees().clear();
		for (Employee emp : employees) {
			processPosition(emp);
			company.addEmployee(emp);
			employeeRepository.save(emp);
		}
		return company;
	}

	private void processPosition(Employee employee) {
		Position position = null;
		String posName = employee.getJob().getNameOfPosition();
		if (posName != null) {
			position = positionRepository.findBynameOfPosition(posName);
			if (position == null) {
				position = positionRepository.save(new Position(posName, null));
			}
		}
		employee.setJob(position);
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
	
	@Transactional
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
