package hu.cubix.spring.hr.gaborh.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public class EmployeeDto {

	private Long id;
	@NotEmpty
	private String name;
	@NotNull
	private PositionDto job;
	@Positive
	private Integer salary;
	@Past
	private LocalDateTime startDate;

	private CompanyDto company;	

	private String username;
	private String password;

	public EmployeeDto() {

	}

	public EmployeeDto(@NotEmpty String name, @NotNull PositionDto job, @Positive Integer salary,
			@Past LocalDateTime startDate) {
		super();
		this.name = name;
		this.job = job;
		this.salary = salary;
		this.startDate = startDate;		
	}

	public EmployeeDto(Long id, @NotEmpty String name, @NotNull PositionDto job, @Positive Integer salary,
			@Past LocalDateTime startDate) {
		super();
		this.id = id;
		this.name = name;
		this.job = job;
		this.salary = salary;
		this.startDate = startDate;		
	}

	public PositionDto getJob() {
		return job;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setJob(PositionDto job) {
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

	public CompanyDto getCompany() {
		return company;
	}

	public void setCompany(CompanyDto company) {
		this.company = company;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
