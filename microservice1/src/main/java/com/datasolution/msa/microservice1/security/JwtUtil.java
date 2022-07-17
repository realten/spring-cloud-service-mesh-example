package com.datasolution.msa.microservice1.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
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
public class JwtUtil {
    private static String SECRET_KEY = "datasolution";
    private static long tokenValidMilisecond = 1000L * 60 * 2;  //1000 * 60 = 1초

    /**
     * Json Web Token을 생성한다.
     *
     * @param claims 속성 정보
     * @return Token 값
     */
    public String createToken(Map<String, Object> claims) {
        String jwtKey = new SigningKeyResolver().encryptionSHA256(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(jwtKey.getBytes());

        String jws = Jwts.builder()
                .setId(UUID.randomUUID().toString())    //토큰 ID
                .setHeaderParam(JwsHeader.KEY_ID, SECRET_KEY)   //JWT HEADER - SECRET_KEY
                .setClaims(claims)                      //속성 정보
                .setIssuedAt(new Date(System.currentTimeMillis()))  //발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidMilisecond)) //만료시간
                .signWith(key, SignatureAlgorithm.HS256)    //HMAC using SHA-256 적용
                .compact();
        log.info("key : Bearer {}", jws);

        return "Bearer " + jws;
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
