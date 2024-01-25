package hu.cubix.spring.hr.gaborh.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Page<Employee> findBySalaryGreaterThan(int limitSalary, Pageable pageable);

	List<Employee> findByJob(Position job);

	@Query("SELECT e FROM Employee e WHERE LOWER(name) like LOWER(CONCAT(:searchPrefix,' %')) OR LOWER(name) like LOWER(CONCAT(:searchPrefix,'. %'))")
	List<Employee> findByNameStartingWithIgnoreCase(String searchPrefix);

//	@Query("SELECT e FROM Employee e WHERE e.startDate BETWEEN :start AND :end ")
	List<Employee> findByStartDateBetween(LocalDateTime start, LocalDateTime end);

	
	@Modifying		//ha UPDATE vagy DELETE van a Query-ben, akkor kell a @Modifying annotáció
	@Query("UPDATE Employee e "
			+ "SET e.salary = :minSalary "
			+ "WHERE e.job.nameOfPosition = :position "
			+ "AND e.company.id = :companyId "
			+ "AND e.salary < :minSalary")
	public void updateSalaries(long companyId, String position, int minSalary);

	Optional<Employee> findByUsername(String username);
}
