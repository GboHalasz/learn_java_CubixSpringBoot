package hu.cubix.spring.hr.gaborh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.ManagerByCompany;

public interface ManagerByCompanyRepository extends JpaRepository<ManagerByCompany, Long> {

	List<ManagerByCompany> findByCompany(Company company);
}