package hu.cubix.spring.hr.gaborh.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.cubix.spring.hr.gaborh.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	List<Employee> findBySalaryGreaterThan(int limitSalary);
	
	List<Employee> findByJob(String job);

//	@Query("SELECT e FROM Employee e WHERE LOWER(name) like LOWER(CONCAT(:searchPrefix,'%')) ")
	List<Employee> findByNameStartingWithIgnoreCase(String searchPrefix);

//	@Query("SELECT e FROM Employee e WHERE e.startDate BETWEEN :start AND :end ")
	List<Employee> findByStartDateBetween(LocalDateTime start, LocalDateTime end);

}
