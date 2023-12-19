package hu.cubix.spring.hr.gaborh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.cubix.spring.hr.gaborh.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
