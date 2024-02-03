package hu.cubix.spring.hr.gaborh.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.cubix.spring.hr.gaborh.config.HrConfigurationProperties;
import hu.cubix.spring.hr.gaborh.config.HrConfigurationProperties.JWTConf;
import hu.cubix.spring.hr.gaborh.model.Employee;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

	private static final String MANAGER = "manager";
	private static final String MANAGED_EMPLOYEES = "managedEmployees";
	private static final String USERNAME = "username";
	private static final String FULL_NAME = "fullName";
	private static final String ID = "id";
	private Algorithm algorithm;

	@Autowired
	private HrConfigurationProperties config;

	@PostConstruct
	public void init() {
		JWTConf jwtConf = config.getJwtConf();
		try {
			Method algorithmMethod = Algorithm.class.getMethod(jwtConf.getAlgorithm(), String.class);
			algorithm = (Algorithm) algorithmMethod.invoke(Algorithm.class, jwtConf.getSecret());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private static final Algorithm algorithm = Algorithm.HMAC256("mysecret");

	public String createJwt(UserDetails userDetails) throws Exception {

		JWTConf jwtConf = config.getJwtConf();

		Employee employee = ((HrUser) userDetails).getEmployee();

		Builder jwtBuilder = JWT.create().withSubject(userDetails.getUsername())
				.withArrayClaim("auth",
						userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
								.toArray(String[]::new))
				.withClaim(FULL_NAME, employee.getName()).withClaim(ID, employee.getId());

		Employee manager = employee.getManager();
		if (manager != null) {
			jwtBuilder.withClaim(MANAGER, createMapFromEmployee(manager));
		}

		List<Employee> managedEmployees = employee.getManagedEmployees();
		if (managedEmployees != null && !managedEmployees.isEmpty()) {
			jwtBuilder.withClaim(MANAGED_EMPLOYEES,
					managedEmployees.stream().map(this::createMapFromEmployee).toList());
		}

		return jwtBuilder
				.withExpiresAt(
						new Date(System.currentTimeMillis() + jwtConf.getExpiryMinutes().toMillis()))
				.withIssuer(jwtConf.getIssuer()).sign(algorithm);
	}

	private Map<String, Object> createMapFromEmployee(Employee employee) {
		return Map.of(ID, employee.getId(), USERNAME, employee.getUsername());
	}

	public UserDetails parseJwt(String jwtToken) throws Exception {

		DecodedJWT decodedJwt = JWT.require(algorithm).withIssuer(config.getJwtConf().getIssuer()).build()
				.verify(jwtToken);

		Employee employee = new Employee();									//egy új inmemory Employeet hozunk létre a tokenből kinyert adatokból, majd ezzel az employee-val létrehozott új HrUser-rel térünk vissza.
		employee.setId(decodedJwt.getClaim(ID).asLong());
		employee.setUsername(decodedJwt.getSubject());
		employee.setName(decodedJwt.getClaim(FULL_NAME).asString());

		Claim managerClaim = decodedJwt.getClaim(MANAGER);					//a visszaadott Claim sosem null

		Map<String, Object> managerData = managerClaim.asMap();				//de ez már lehet null
		employee.setManager(parseEmployeeFromMap(managerData));				//ezért a parseEmployeeFromMap-ban ellenőrizzük, hogy null-e

		Claim managedEmployeesClaim = decodedJwt.getClaim(MANAGED_EMPLOYEES);
		employee.setManagedEmployees(new ArrayList<>());
		List<HashMap> managedEmployeesMaps = managedEmployeesClaim.asList(HashMap.class);
		if (managedEmployeesMaps != null) {
			for (var employeeMap : managedEmployeesMaps) {
				Employee managedEmployee = parseEmployeeFromMap(employeeMap);
				if (managedEmployee != null)
					employee.getManagedEmployees().add(managedEmployee);
			}
		}

		return new HrUser(decodedJwt.getSubject(), "dummy",
				decodedJwt.getClaim("auth").asList(String.class).stream().map(SimpleGrantedAuthority::new).toList(),
				employee);
	}

	private Employee parseEmployeeFromMap(Map<String, Object> employeeMap) {
		if (employeeMap != null) {
			Employee employee = new Employee();
			employee.setId(((Integer) employeeMap.get(ID)).longValue()); //hiába long volt amikor beletettük a Claim-be a Map-et, a könyvtár default sajátossága, hogy átalakítja integerré és kinyerésnél vissza kell alakítanunk long-gá
			employee.setUsername((String) employeeMap.get(USERNAME));
		}

		return null;
	}
}
