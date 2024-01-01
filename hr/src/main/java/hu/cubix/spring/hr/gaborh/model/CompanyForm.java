package hu.cubix.spring.hr.gaborh.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class CompanyForm {

	@Id @GeneratedValue
	private long id;
	private String name;
	
	public CompanyForm() {
		
	}

	public CompanyForm(String name) {
		super();		
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String companyForm) {
		this.name = companyForm;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyForm other = (CompanyForm) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return name;
	}
			
}
