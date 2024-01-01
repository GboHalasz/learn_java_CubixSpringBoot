package hu.cubix.spring.hr.gaborh.dto;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {

	private Long id;

	private Long registrationNumber;

	private String name;

	private String address;

	private List<EmployeeDto> employees = new ArrayList<>();

	private CompanyFormDto companyForm;

	public CompanyDto() {

	}

	public CompanyDto(Long id, Long registrationNumber, String name, String address, List<EmployeeDto> employees,
			CompanyFormDto companyForm) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.employees = employees;
		this.companyForm = companyForm;
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

	public CompanyFormDto getCompanyForm() {
		return companyForm;
	}

	public void setCompanyForm(CompanyFormDto companyForm) {
		this.companyForm = companyForm;
	}

}
