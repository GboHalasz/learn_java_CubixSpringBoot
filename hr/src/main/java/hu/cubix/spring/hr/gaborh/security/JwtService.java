package hu.cubix.spring.hr.gaborh.security;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.cubix.spring.hr.gaborh.config.HrConfigurationProperties;
import hu.cubix.spring.hr.gaborh.config.HrConfigurationProperties.JWTConf;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;

@Service
public class JwtService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private HrConfigurationProperties config;

//	private static final Algorithm algorithm = Algorithm.HMAC256("mysecret");
	
	public JwtService() throws Exception {
		super();
	}

	public String createJwt(UserDetails userDetails) throws Exception {
		JWTConf jwtConf = config.getJwtConf();
		Method algorithmMethod = Algorithm.class.getMethod(jwtConf.getAlgorithm(), String.class);
		Algorithm algorithm =(Algorithm) algorithmMethod.invoke(Algorithm.class, jwtConf.getSecret());	
		
		
		Employee employee = employeeRepository.findByUsername(userDetails.getUsername()).get();

		return JWT.create() // composes the token
				.withSubject(userDetails.getUsername())
				.withArrayClaim("auth",
						userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
								.toArray(String[]::new))
				.withClaim("employeeName", employee.getName()).withClaim("employeeId", employee.getId())
				.withClaim("managedEmployees", getManagedEmployeesOf(employee))
				.withClaim("managerId", employee.getManager().getId())
				.withClaim("managerUsername", employee.getManager().getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(jwtConf.getExpiryMinutes()))) // generally we can
																									// set 5 - 10
																									// minutes, but here
																									// I set 2
																									// minutes for test
																									// purposes
				.withIssuer(jwtConf.getIssuer()).sign(algorithm);
	}

	public UserDetails parseJwt(String jwtToken) throws Exception {
		JWTConf jwtConf = config.getJwtConf();
		Method algorithmMethod = Algorithm.class.getMethod(jwtConf.getAlgorithm(), String.class);
		Algorithm algorithm =(Algorithm) algorithmMethod.invoke(Algorithm.class, jwtConf.getSecret());
		
		DecodedJWT decodedJwt = JWT.require(algorithm).withIssuer(jwtConf.getIssuer()).build().verify(jwtToken);

		return new HrUser(decodedJwt.getSubject(), "dummy",
				decodedJwt.getClaim("auth").asList(String.class).stream().map(SimpleGrantedAuthority::new).toList(),
				decodedJwt.getClaim("employeeName").asString(), decodedJwt.getClaim("employeeId").asLong(),
				convertToMapStringLong(decodedJwt.getClaim("managedEmployees").asMap()),
				decodedJwt.getClaim("managerId").asLong(), decodedJwt.getClaim("managerUsername").asString());
	}

	public Map<String, Long> getManagedEmployeesOf(Employee employee) {
		List<Employee> managedEmployees = employeeRepository.findByManagerId(employee.getId());
		Map<String, Long> managedEmployeesMap = new HashMap<String, Long>();
		if (!managedEmployees.isEmpty()) {
			for (Employee emp : managedEmployees) {

				managedEmployeesMap.put(emp.getUsername(), emp.getId());
			}
		}
		return managedEmployeesMap;
	}

	private Map<String, Long> convertToMapStringLong(Map<String, Object> map) {
		Map<String, Long> newMap = map.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> Long.valueOf(e.getValue().toString())));
		return newMap;
	}
}
