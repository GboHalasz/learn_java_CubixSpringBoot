package hu.cubix.spring.hr.gaborh.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

@Entity
public class Employee {

	@Id
	@GeneratedValue
	private long id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String job;
	@Positive
	private Integer salary;
	@Past
	private LocalDateTime startDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "company_id")
	@JsonBackReference
	private Company company;

	public Employee() {

	}

	public Employee(long id, @NotEmpty String name, @NotEmpty String job, @Positive Integer salary,
			@Past LocalDateTime startDate, Company company) {
		super();
		this.id = id;
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

	public String getJob() {
		return job;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setJob(String job) {
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
}
