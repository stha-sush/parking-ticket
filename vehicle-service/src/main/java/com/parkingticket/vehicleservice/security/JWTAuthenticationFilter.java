package com.parkingticket.vehicleservice.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res,
			@NonNull FilterChain chain) throws ServletException, IOException {
		String path = req.getRequestURI();

		// Allow actuator or other public paths if needed
		if (path.startsWith("/actuator")) {
			chain.doFilter(req, res);
			return;
		}

		String header = req.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			logger.warn("Missing or invalid Authorization header");
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		String token = header.substring(7);
		logger.info("Incoming token [{}]", token);

		int statusCode = jwtService.validateToken(token);
		logger.info("Token validation status [{}]", statusCode);

		if (statusCode != 200) {
			res.setStatus(statusCode);
			return;
		}

		JwtTokenContext.setToken(token);
		try {
			chain.doFilter(req, res);
		} finally {
			JwtTokenContext.clearToken();
		}
	}
}