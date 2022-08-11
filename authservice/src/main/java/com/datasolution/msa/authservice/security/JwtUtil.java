package com.datasolution.msa.authservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Json Web Token 방식 인증 처리
 * 	- HTTP authentication
 * 		- Bearer
 */
@Slf4j
public class JwtUtil implements JwtKey {
    // 1000L * 60 = 1분
    private static long accessTokenValidMilisecond = 1000L * 60 * 2;  // 2분
    private static long refreshTokenValidMilisecond = 1000L * 60 * 60 * 24 * 14;  // 2주

    /**
     * Access Json Web Token을 생성한다.
     *
     * @param params 속성 정보
     * @param bearerAppendYn Bearer Append Yn
     * @return Token 값
     */
    public String createAccessToken(Map<String, Object> params, boolean bearerAppendYn) {
        String jwtKey = new SigningKeyResolver().encryptionSHA256(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(jwtKey.getBytes());

        Claims claims = Jwts.claims()
                .setId(UUID.randomUUID().toString())    //토큰 ID
                .setIssuedAt(new Date(System.currentTimeMillis()))  //발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidMilisecond)); //만료시간

        for(String k : params.keySet()) {
            claims.put(k, params.get(k));
        }

        String jws = Jwts.builder()
                .setClaims(claims)                               //속성 정보
                .signWith(key, SignatureAlgorithm.HS256)         //HMAC using SHA-256 적용
                .compact();
        log.info("Access Token  : Bearer {}", jws);

        return isBearerAppend(bearerAppendYn) + jws;
    }

    /**
     * Refresh Json Web Token을 생성한다.
     *
     * @param bearerAppendYn Bearer Append Yn
     * @return Token 값
     */
    public String createRefreshToken(boolean bearerAppendYn) {
        String jwtKey = new SigningKeyResolver().encryptionSHA256(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(jwtKey.getBytes());

        Claims claims = Jwts.claims()
                .setId(UUID.randomUUID().toString())    //토큰 ID
                .setIssuedAt(new Date(System.currentTimeMillis()))  //발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidMilisecond)); //만료시간

        String jws = Jwts.builder()
                .setClaims(claims)                               //속성 정보
                .signWith(key, SignatureAlgorithm.HS256)         //HMAC using SHA-256 적용
                .compact();
        log.info("Refresh Token : Bearer {}", jws);


        return isBearerAppend(bearerAppendYn) + jws;
    }

    /**
     * Bearer String Append
     *
     * @param b Append Yn
     * @return
     */
    private String isBearerAppend(boolean b) {
        return b ? "Bearer " : "";
    }

    /**
     * 받아온 Json Web Token 키 정보를 검증한다.
     *
     * @param authorizationValue
     * @return Claims 정보
     */
    public Jws<Claims> verifyToken(String authorizationValue) throws Exception {
        log.info("authorizationValue = {}", authorizationValue);

        /* 인증 값 검증 */
        if(ObjectUtils.isEmpty(authorizationValue) || !authorizationValue.startsWith("Bearer")) {
            throw new Exception("인증 값이 없음");
        }

        /* Bearer 제거한 인증 값 */
        String jwtToken = StringUtils.trim(authorizationValue.replace("Bearer", ""));

        /* Token 값 검증 */
        if(ObjectUtils.isEmpty(jwtToken)) {
            throw new Exception("Token 값이 없음");
        }

        Jws<Claims> jwsClaims = null;
        try {
            jwsClaims = Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolver())
                    .build().parseClaimsJws(jwtToken);
        } catch (ExpiredJwtException e) {
            throw new Exception("유효 기간이 지난 JWT를 수신한 경우");	//유효 기간이 지난 JWT를 수신한 경우
        } catch (MalformedJwtException e) {
            throw new Exception("구조적인 문제가 있는 JWT인 경우");	//구조적인 문제가 있는 JWT인 경우
        } catch (SecurityException e) {
            throw new Exception("시그너처 연산이 실패하였거나, JWT의 시그너처 검증이 실패한 경우");	//시그너처 연산이 실패하였거나, JWT의 시그너처 검증이 실패한 경우
        } catch (Exception e) {
            log.error("JWT Exception : {}", e.getMessage());
        }

        return jwsClaims;
    }
}
