package com.parkingticket.vehicleservice.security;

import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	public static final String SECRET = "9347566A59703373367639792D423F4528482B4D6251655487246D5A71347437";

	public int validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
			return 200; // valid
		} catch (UnsupportedJwtException | MalformedJwtException e) {
			return 403; // invalid
		} catch (ExpiredJwtException e) {
			return 409; // expired
		}
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
