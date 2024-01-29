package hu.cubix.spring.hr.gaborh.service;

import static hu.cubix.spring.hr.gaborh.service.EmployeeSpecifications.companyStartsWith;
import static hu.cubix.spring.hr.gaborh.service.EmployeeSpecifications.hasId;
import static hu.cubix.spring.hr.gaborh.service.EmployeeSpecifications.hasNameOfPosition;
import static hu.cubix.spring.hr.gaborh.service.EmployeeSpecifications.hasSameEntryDay;
import static hu.cubix.spring.hr.gaborh.service.EmployeeSpecifications.nameStartsWith;
import static hu.cubix.spring.hr.gaborh.service.EmployeeSpecifications.salaryInFivePercent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.Qualification;
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
//		if (employeeRepository.existsById(employee.getId()))
//			return null;
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
		Qualification posQuali = employee.getJob().getQualification();	
		if (posName != null) {
			position = positionRepository.findBynameOfPosition(posName);
			if (position == null) {
				position = positionRepository.save(new Position(posName, posQuali));
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

	@Override
	public List<Employee> findEmployeesByExample(Employee employee) {

		Long id = employee.getId();
		String name = employee.getName();
		String nameOfPosition = employee.getJob().getNameOfPosition();
		Integer salary = employee.getSalary();
		LocalDate entryDate = (employee.getStartDate() != null) ? employee.getStartDate().toLocalDate() : null;
		String companyName = employee.getCompany().getName();

		Specification<Employee> specs = Specification.where(null);

		if (id > 0) {
			specs = specs.and(hasId(id));
		}

		if (StringUtils.hasLength(name)) {
			specs = specs.and(nameStartsWith(name));
		}

		if (StringUtils.hasLength(nameOfPosition)) {
			specs = specs.and(hasNameOfPosition(nameOfPosition));
		}

		if (salary != null && salary > 0) {
			specs = specs.and(salaryInFivePercent(salary));
		}

		if (entryDate != null) {
			specs = specs.and(hasSameEntryDay(entryDate));
		}

		if (StringUtils.hasLength(companyName)) {
			specs = specs.and(companyStartsWith(companyName));
		}

		return employeeRepository.findAll(specs);
	}

}
