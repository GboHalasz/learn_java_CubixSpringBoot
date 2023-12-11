package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Employee;

@Service
public abstract class EmployeeSuperService implements EmployeeService {

	private Map<Long, Employee> employees = new HashMap<>();

	{
		employees.put(1L, new Employee(1L, "Pál Dénes", "developer", 10000, LocalDateTime.of(1990, 01, 12, 8, 00)));
		employees.put(2L, new Employee(2L, "Jane Doe", "developer", 20000, LocalDateTime.of(1990, 01, 12, 8, 00)));
		employees.put(3L, new Employee(3L, "Kiss Elemér", "developer", 30000, LocalDateTime.of(1990, 01, 12, 8, 00)));
	}

	public Employee create(Employee employee) {
		if (findById(employee.getId()) != null) {
			return null;
		}
		return save(employee);
	}
	
	public Employee update(Employee employee) {                                                                                                                                                                            
		if (findById(employee.getId()) == null) {
			return null;
		}
		return save(employee);
	 }


	public Employee save(Employee employee) {
		employees.put(employee.getId(), employee);
		return employee;
	}

	public List<Employee> findAll() {
		return new ArrayList<>(employees.values());
	}

	public Employee findById(long id) {
		return employees.get(id);
	}

	public void delete(long id) {
		employees.remove(id);
	}

}
