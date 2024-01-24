package hu.cubix.spring.hr.gaborh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.CompanyForm;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.PositionDetailsByCompany;
import hu.cubix.spring.hr.gaborh.repository.CompanyFormRepository;
import hu.cubix.spring.hr.gaborh.repository.CompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import hu.cubix.spring.hr.gaborh.repository.ManagerByCompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionDetailsByCompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionRepository;
import hu.cubix.spring.hr.gaborh.repository.TimeOffRequestRepository;
import jakarta.transaction.Transactional;

@Service
public class InitDbService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyFormRepository companyFormRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

	@Autowired
	ManagerByCompanyRepository managerByCompanyRepository;
	
	@Autowired
	TimeOffRequestRepository timeOffRequestRepository;

	@Transactional
	public void clearDB() {
		managerByCompanyRepository.deleteAllInBatch();
		timeOffRequestRepository.deleteAllInBatch();
		employeeRepository.deleteAllInBatch();
		positionDetailsByCompanyRepository.deleteAllInBatch();
		companyRepository.deleteAllInBatch();
		companyFormRepository.deleteAllInBatch();
		positionRepository.deleteAllInBatch();
	}

	public List<Employee> insertTestData(List<Position> positions, List<CompanyForm> companyForms,
			List<Company> companies, List<Employee> employees, List<PositionDetailsByCompany> testPDBCList) {
		positionRepository.saveAll(positions);
		companyFormRepository.saveAll(companyForms);
		companyRepository.saveAll(companies);
		List<Employee> savedEmployees = employeeRepository.saveAll(employees);
		positionDetailsByCompanyRepository.saveAll(testPDBCList);

		return savedEmployees;
	}
}
