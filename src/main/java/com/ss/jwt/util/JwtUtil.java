package com.ss.jwt.util;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	private static final String SECRET_KEY = "your-secret-key";
    private static final long EXPIRATION_TIME = 18000000; // 5 hours

    public static String generateToken(String subject) {
    	Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String getSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public static boolean validateToken(String token) {
        try {
        	System.out.println("token---"+token);
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
}
