package hu.cubix.spring.hr.gaborh.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyDto {

	@JsonProperty("id")
	private String id;
	@JsonProperty("registrationNumber")
	private Long registrationNumber;
	@JsonProperty("name")
	private String name;
	@JsonProperty("address")
	private String address;
	@JsonProperty("workers")
	private Map<Long, EmployeeDto> workers = new HashMap<>();

	public CompanyDto(String id, Long registrationNumber, String name, String address) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
