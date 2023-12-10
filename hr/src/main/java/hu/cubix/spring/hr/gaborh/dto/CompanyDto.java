package hu.cubix.spring.hr.gaborh.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;

public class CompanyDto {

	@JsonView(Views.BaseData.class)
	private Long id;
	@JsonView(Views.BaseData.class)
	private Long registrationNumber;
	@JsonView(Views.BaseData.class)
	private String name;
	@JsonView(Views.BaseData.class)
	private String address;
	
	private Map<Long, EmployeeDto> workers = new HashMap<>();

	public CompanyDto(Long id, Long registrationNumber, String name, String address, Map<Long, EmployeeDto> workers) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.workers = workers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(Long registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Map<Long, EmployeeDto> getWorkers() {
		return workers;
	}

	public void setWorkers(Map<Long, EmployeeDto> workers) {
		this.workers = workers;
	}

}
