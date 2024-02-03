package hu.cubix.spring.hr.gaborh.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	private static final String BEARER = "Bearer ";
	private static final String AUTHORIZATION = "Authorization";

	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(AUTHORIZATION);

		if (authHeader != null && authHeader.startsWith(BEARER)) {
			String jwtToken = authHeader.substring(BEARER.length());

			UserDetails userDetails;
			try {
				userDetails = jwtService.parseJwt(jwtToken);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 	// besides Principal
																										// this will set
																										// the other parts
																										// of the object,
																										// e.g.
																										// sessionId (if
																										// exists)
																										// (this line could
																										// be
																										// omitted here,
																										// but the
																										// authentication
																										// object
																										// will be complete
																										// with it)
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				return;	
			}
			
			

			
		}

		filterChain.doFilter(request, response); // it lets continue to the filterChain
	}
}
