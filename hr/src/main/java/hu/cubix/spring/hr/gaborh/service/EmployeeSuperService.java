package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public Employee create(Employee employee) {
		if (employeeRepository.existsById(employee.getId()))
			return null;
		return save(employee);
	}
		
	@Override
	@Transactional
	public Employee update(Employee employee) {
		if (!employeeRepository.existsById(employee.getId()))
			return null;
		
		return save(employee);
	}
	
	@Override
	@Transactional
	public Employee save(Employee employee) {
		processPosition(employee);
		return employeeRepository.save(employee);
	}
	
	private void processPosition(Employee employee) {
		Position position = null;
		String posName = employee.getJob().getNameOfPosition();
		if(posName != null) {
			position = positionRepository.findBynameOfPosition(posName);
			if(position == null) {
				position = positionRepository.save(new Position(posName, null));
			}
		}
		employee.setJob(position);
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
	@Transactional
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}
	
	@Override
	public Page<Employee> findBySalaryGreaterThan(int limitSalary, Pageable pageable) {
		return employeeRepository.findBySalaryGreaterThan(limitSalary, pageable);
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
