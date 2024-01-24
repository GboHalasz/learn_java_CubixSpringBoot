package hu.cubix.spring.hr.gaborh.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ManagerByCompany {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private Employee manager;
	
	@ManyToOne
	private Company company;
	
	public ManagerByCompany() {
		super();
	}

	public ManagerByCompany(Employee manager, Company company) {
		super();
		this.manager = manager;
		this.company = company;		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
