package com.parkingticket.ticket_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.parkingticket.ticket_service.DTO.AuthRequest;
import com.parkingticket.ticket_service.DTO.AuthResp;

@Service
public class AuthService {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	private static Logger logger = LoggerFactory.getLogger(AuthService.class);

	public AuthResp validateAuthRequest(AuthRequest authRequest) {
		AuthResp resp = new AuthResp();

		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		if (authenticate.isAuthenticated()) {
			String token = jwtService.generateToken(authRequest.getUsername());
			resp.setRespCode(200);
			resp.setRespDesc("Success");
			resp.setToken(token);
		} else {
			resp.setRespCode(403);
			resp.setRespDesc("Invalid Username");
		}

		return resp;
	}

	public int validateToken(String token) {
		logger.info("token [{}]", token);
		int respCode = jwtService.validateToken(token);
		logger.info("respCode [{}]", respCode);
		return respCode;
	}
}
