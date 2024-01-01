package hu.cubix.spring.hr.gaborh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.spring.hr.gaborh.dto.AvsPJDto;
import hu.cubix.spring.hr.gaborh.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	@Query("SELECT e.company FROM Employee e WHERE e.salary > :minSalary AND company IS NOT NULL")
	List<Company> findByEmployeesNotEmptyAndEmployeeSalaryGreaterThan(long minSalary);

	@Query("SELECT e.company FROM Employee e WHERE e.company IS NOT null GROUP BY e.company HAVING COUNT(e.company) > :limit")
	List<Company> findByEmployeesSizeGreaterThan(long limit);
	
	@Query("select new hu.cubix.spring.hr.gaborh.dto.AvsPJDto(e.job.nameOfPosition as job, AVG(e.salary) as avs) from Employee e where e.company.id = :companyId group by e.job order by avs desc")
	List<AvsPJDto> averageSalaryOfEmployeesGrouppedByJobAt(long companyId);	
	
}
