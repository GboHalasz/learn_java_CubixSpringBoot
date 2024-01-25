package hu.cubix.spring.hr.gaborh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.spring.hr.gaborh.dto.AvsPJDto;
import hu.cubix.spring.hr.gaborh.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	@EntityGraph(attributePaths = {"employees", "companyForm", "employees.job"})
	@Query("SELECT c FROM Company c")
	public List<Company> findAllWithEmployees();
	
	@Query("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.employees WHERE c.id = :cId")
	public Optional<Company> findByIdWithEmployees(long cId);	
	
	@Query("SELECT DISTINCT c FROM Company c JOIN c.employees e WHERE e.salary > :minSalary")
	List<Company> findByEmployeesNotEmptyAndEmployeeSalaryGreaterThan(long minSalary);

	@Query("SELECT c FROM Company c WHERE SIZE(c.employees) > :limit")
	List<Company> findByEmployeesSizeGreaterThan(long limit);
	
	@Query("SELECT new hu.cubix.spring.hr.gaborh.dto.AvsPJDto(e.job.nameOfPosition AS job, AVG(e.salary) AS avs) from Employee e where e.company.id = :companyId group by e.job order by avs desc")
	List<AvsPJDto> findAverageSalariesByPosition(long companyId);	
	
}
