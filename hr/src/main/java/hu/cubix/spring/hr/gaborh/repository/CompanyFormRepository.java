package hu.cubix.spring.hr.gaborh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.cubix.spring.hr.gaborh.model.CompanyForm;

public interface CompanyFormRepository extends JpaRepository<CompanyForm, Long> {

	CompanyForm findByname(String name);
}
