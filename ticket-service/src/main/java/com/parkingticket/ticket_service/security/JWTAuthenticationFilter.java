package com.parkingticket.ticket_service.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		String path = req.getRequestURI();
		// Optional: allow health/check or Swagger without auth
		if (path.startsWith("/actuator")) {
			chain.doFilter(req, res);
			return;
		}

		String header = req.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		String token = header.substring(7);
		logger.info("token [{}]", token);

		int statusCode = jwtService.validateToken(token);
		logger.info("statusCode [{}]", statusCode);

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
