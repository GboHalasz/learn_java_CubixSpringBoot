package hu.cubix.spring.hr.gaborh.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Employee {

	@Id
	@GeneratedValue
	private long id;
	private String name;

	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position job;
	private Integer salary;
	private LocalDateTime startDate;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	public Employee() {

	}

	public Employee(String name, Position job, Integer salary,
			LocalDateTime startDate, Company company) {
		super();
		this.name = name;
		this.job = job;
		this.salary = salary;
		this.startDate = startDate;
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getJob() {
		return job;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setJob(Position job) {
		this.job = job;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
		Employee other = (Employee) obj;
		return id == other.id;
	}
}
