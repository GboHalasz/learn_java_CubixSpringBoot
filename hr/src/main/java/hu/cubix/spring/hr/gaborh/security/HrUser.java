package hu.cubix.spring.hr.gaborh.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import hu.cubix.spring.hr.gaborh.model.Employee;

public class HrUser extends User {

	private Employee employee;

	public HrUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
			Employee employee) {
		super(username, password, authorities);
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}