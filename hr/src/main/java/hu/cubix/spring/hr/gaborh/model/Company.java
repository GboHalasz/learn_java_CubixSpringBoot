package hu.cubix.spring.hr.gaborh.model;

import java.util.HashMap;
import java.util.Map;

public class Company {


	
	private Long id;
	private Long registrationNumber;
	private String name;
	private String address;
	private Map<Long, Employee> workers = new HashMap<>();

	public Company(Long id, Long registrationNumber, String name, String address, Map<Long, Employee> workers) {
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

	public Map<Long, Employee> getWorkers() {
		return workers;
	}

	public void setWorkers(Map<Long, Employee> workers) {
		this.workers = workers;
	}

}
