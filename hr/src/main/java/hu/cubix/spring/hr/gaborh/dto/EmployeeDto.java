package hu.cubix.spring.hr.gaborh.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public class EmployeeDto {

	private long id;
	@NotEmpty
	private String name;
	@NotNull
	private PositionDto job;
	@Positive
	private Integer salary;
	@Past
	private LocalDateTime startDate;

	public EmployeeDto() {

	}

	public EmployeeDto(long id, String name, PositionDto job, Integer salary,
			LocalDateTime startDate) {
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

}
