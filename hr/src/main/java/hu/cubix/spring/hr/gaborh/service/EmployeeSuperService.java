package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;

@Service
public abstract class EmployeeSuperService implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee create(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee update(Employee employee) {
		if (!employeeRepository.existsById(employee.getId()))
			return null;

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
	public List<Employee> findByJob(String job) {
		return employeeRepository.findByJob(job);
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
