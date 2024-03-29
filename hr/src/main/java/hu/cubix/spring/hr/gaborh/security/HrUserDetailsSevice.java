package hu.cubix.spring.hr.gaborh.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;

@Service
public class HrUserDetailsSevice implements UserDetailsService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Employee employee = employeeRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return new HrUser(username, employee.getPassword(), Arrays.asList(new SimpleGrantedAuthority("USER")), employee);
	}
}
