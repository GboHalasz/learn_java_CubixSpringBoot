package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionRepository;

@Service
public abstract class EmployeeSuperService implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;
	
	@Override
	public Employee create(Employee employee) {
		if (employeeRepository.existsById(employee.getId()))
			return null;
		return save(employee);
	}
		
	@Override
	public Employee update(Employee employee) {
		if (!employeeRepository.existsById(employee.getId()))
			return null;
		
		return save(employee);
	}
	
	@Override
	public Employee save(Employee employee) {
		Position position = positionRepository.findBynameOfPosition(employee.getJob().getNameOfPosition());		
		if (position == null) {
			return null;
		}
		employee.setJob(position);
		return employeeRepository.save(employee);
	}
	

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@Override
	public Optional<Employee> findById(long id) {
		return employeeRepository.findById(id);
	}

	@Override
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}
	
	@Override
	public List<Employee> findBySalaryGreaterThan(int limitSalary) {
		return employeeRepository.findBySalaryGreaterThan(limitSalary);
	}

	@Override
	public List<Employee> findByJob(String jobName) {
		Position position = positionRepository.findBynameOfPosition(jobName);		
		if (position == null) {
			return null;
		}
		return employeeRepository.findByJob(position);
	}

	@Override
	public List<Employee> findByNamePrefix(String searchPrefix) {
		return employeeRepository.findByNameStartingWithIgnoreCase(searchPrefix);
	}

	@Override
	public List<Employee> findByStartDateBetween(LocalDateTime start, LocalDateTime end) {
		return employeeRepository.findByStartDateBetween(start, end);
	}

}
