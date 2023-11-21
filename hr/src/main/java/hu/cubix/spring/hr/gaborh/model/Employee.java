package hu.cubix.spring.hr.gaborh.model;

import java.time.LocalDateTime;

public class Employee {
	
	private Long id;
	private String job;
	private Integer salary;
	private LocalDateTime startDate;
	
	public Employee(Long id, String job, Integer salary, LocalDateTime startDate) {
		super();
		this.id = id;
		this.job = job;
		this.salary = salary;
		this.startDate = startDate;
	}

	public String getJob() {
		return job;
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
