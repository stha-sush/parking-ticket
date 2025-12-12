package com.parkingticket.ticket_service.security;

public class JwtTokenContext {

	private static final ThreadLocal<String> token = new ThreadLocal<>();

	public static void setToken(String jwtToken) {
		token.set(jwtToken);
	}

	public static String getToken() {
		return token.get();
	}

	public static void clearToken() {
		token.remove();
	}
}
