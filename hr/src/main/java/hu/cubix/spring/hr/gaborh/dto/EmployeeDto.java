package hu.cubix.spring.hr.gaborh.dto;

import java.time.LocalDateTime;

public class EmployeeDto {

	private Long id;
	private String name;	
	private String job;
	private Integer salary;
	private LocalDateTime startDate;

	public EmployeeDto() {

	}

	public EmployeeDto(Long id, String name, String job, Integer salary, LocalDateTime startDate) {
		super();
		this.id = id;
		this.job = job;
		this.salary = salary;
		this.startDate = startDate;
	}

	public String getJob() {
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

}
