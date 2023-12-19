package hu.cubix.spring.hr.gaborh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.repository.CompanyRepository;
import jakarta.transaction.Transactional;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Transactional
	public Company create(Company company) {
		if (findById(company.getId()) != null) {
			return null;
		}
		return save(company);
	}

	@Transactional
	public Company update(Company company) {
		if (findById(company.getId()) == null) {
			return null;
		}
		return save(company);
	}

	public Company save(Company company) {
		return companyRepository.save(company);
	}

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public Company findById(long id) {
		return companyRepository.findById(id).orElse(null);
	}

	@Transactional
	public void delete(long id) {
		companyRepository.deleteById(id);
	}

}
