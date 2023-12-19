package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import jakarta.transaction.Transactional;

@Service
public abstract class EmployeeSuperService implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Transactional
	public Employee create(Employee employee) {
		if (findById(employee.getId()) != null) {
			return null;
		}
		return save(employee);
	}

	@Transactional
	public Employee update(Employee employee) {
		if (findById(employee.getId()) == null) {
			return null;
		}
		return save(employee);
	}

	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Employee findById(long id) {
		return employeeRepository.findById(id).orElse(null);
	}

	@Transactional
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}

	public List<Employee> findByJob(String job) {
		return employeeRepository.findByJob(job);
	}

	public List<Employee> findByNamePrefix(String searchPrefix) {
		return employeeRepository.findByNameStartingWithSearchPrefixIgnoreCase(searchPrefix);
	}
	
	public List<Employee> findByStartDateBetween(LocalDateTime start, LocalDateTime end) {
		return employeeRepository.findByStartDateBetween(start, end);
	}

}
