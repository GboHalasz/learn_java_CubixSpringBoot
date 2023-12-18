package hu.cubix.spring.hr.gaborh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Company;

@Service
public class CompanyService {

	private Map<Long, Company> companies = new HashMap<>();

	{
		companies.put(1L, new Company(1L, 1L, "BigCompany", "1100 Budapest, Cél utca 6.", new HashMap<>()));
		companies.put(2L, new Company(2L, 2L, "MediumCompany", "3100 Miskolc, Eldugott utca 120.", new HashMap<>()));
		companies.put(3L, new Company(3L, 3L, "SmallCompany", "1100 Zalaegerszeg, Bécsi út 1.", new HashMap<>()));
	}

	public Company create(Company company) {
		if (findById(company.getId()) != null) {
			return null;
		}
		return save(company);
	}

	public Company update(Company company) {
		if (findById(company.getId()) == null) {
			return null;
		}
		return save(company);
	}

	public Company save(Company company) {
		companies.put(company.getId(), company);
		return company;
	}

	public List<Company> findAll() {
		return new ArrayList<>(companies.values());
	}

	public Company findById(long id) {
		return companies.get(id);
	}

	public void delete(long id) {
		companies.remove(id);
	}

}
