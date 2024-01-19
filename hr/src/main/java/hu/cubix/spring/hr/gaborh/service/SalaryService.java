package hu.cubix.spring.hr.gaborh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionDetailsByCompanyRepository;

@Service
public class SalaryService {
	
	private EmployeeService employeeService;
	
	public SalaryService(EmployeeService employeeService) {		
		this.employeeService = employeeService;
	}

	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	public void setNewSalaryForAn(Employee employee) {
	Integer newSalary = employee.getSalary() + employee.getSalary() * employeeService.getPayRaisePercent(employee) / 100;
	
 		employee.setSalary(newSalary);		
	}
	
	@Transactional
	public void raiseMinSalary(long companyId, String position, int minSalary) {
		positionDetailsByCompanyRepository.findByPositionNameOfPositionAndCompanyId(position, companyId)
			.forEach(pd -> {
				pd.setMinSalary(minSalary);
				//1. megoldás: egyenként SQL UPDATE-eket generál --> nem hatákony
//				pd.getCompany().getEmployees().forEach(e -> {
//					if(e.getPosition().getNameOfPosition().equals(position) 
//							&& e.getSalary() < minSalary) {
//						e.setSalary(minSalary);
//					}
//				});
				//2. megoldás: egyetlen UPDATE query
				employeeRepository.updateSalaries(companyId, position, minSalary);
			});
	}

}
