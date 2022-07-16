package com.datasolution.msa.microservice1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtil {
    private static String SECRET_KEY = "datasolution";
    private static long tokenValidMilisecond = 1000L * 60 * 2;

    private String createToken(Map<String, Object> claims) {
        String secretKeyEncodeBase64 = Encoders.BASE64.encode(SECRET_KEY.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .signWith(key)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidMilisecond))
                .compact();
    }
}
