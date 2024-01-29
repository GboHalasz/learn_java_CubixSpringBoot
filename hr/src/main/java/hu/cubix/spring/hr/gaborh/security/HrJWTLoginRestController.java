package hu.cubix.spring.hr.gaborh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.cubix.spring.hr.gaborh.dto.LoginDto;

@RestController
@RequestMapping("/api/login")
public class HrJWTLoginRestController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtService jwtService;

	@PostMapping
	public String login(@RequestBody LoginDto loginDto) throws Exception {
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		return jwtService.createJwt((UserDetails) authentication.getPrincipal());
	}
}
