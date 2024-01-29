package hu.cubix.spring.hr.gaborh.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class HrUser extends User {

	private String name;
	private Long id;
	private Map<String, Long> managedEmployees;
	private Long managerId;
	private String managerUsername;

	public HrUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String name,
			Long id, Map<String, Long> managedEmployees, Long managerId, String managerUsername) {
		super(username, password, authorities);
		this.name = name;
		this.id = id;
		this.managedEmployees = managedEmployees;
		this.managerId = managerId;
		this.managerUsername = managerUsername;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Long> getManagedEmployees() {
		return managedEmployees;
	}

	public void setManagedEmployees(Map<String, Long> managedEmployees) {
		this.managedEmployees = managedEmployees;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerUsername() {
		return managerUsername;
	}

	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}

}