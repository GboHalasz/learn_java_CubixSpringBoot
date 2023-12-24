package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import hu.cubix.spring.hr.gaborh.model.Employee;

public interface EmployeeService {
	
public Employee create(Employee employee);
	
	public Employee update(Employee employee);
	
	public List<Employee> findAll();
	
	public Optional<Employee> findById(long id);
	
	public void delete(long id);

	int getPayRaisePercent(Employee employee);
	
	public List<Employee> findBySalaryGreaterThan(int limitSalary);
	
	public List<Employee> findByJob(String job);
	
	public List<Employee> findByNamePrefix(String searchPrefix);
	
	public List<Employee> findByStartDateBetween(LocalDateTime start, LocalDateTime end);
}
