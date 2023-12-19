package hu.cubix.spring.hr.gaborh.dto;

import java.util.List;

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

	private List<EmployeeDto> employees;

	public CompanyDto(Long id, Long registrationNumber, String name, String address, List<EmployeeDto> employees) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.employees = employees;
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

	public List<EmployeeDto> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}

}
