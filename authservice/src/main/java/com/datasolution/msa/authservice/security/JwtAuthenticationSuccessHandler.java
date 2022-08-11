package com.datasolution.msa.authservice.security;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private String defaultSuccessUrl = "/";

    public JwtAuthenticationSuccessHandler() {

    }

    public JwtAuthenticationSuccessHandler(String defaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        JwtUtil jwtUtil = new JwtUtil();
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username", authentication.getPrincipal());
        String accessToken = jwtUtil.createAccessToken(claimMap, true);
        String refreshToken = jwtUtil.createRefreshToken(false);

        Cookie cookie = new Cookie("Bearer", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);

        Map<String, Object> result = new HashMap<>();
        result.put("status", true);
        result.put("jwt", accessToken);
        result.put("url", defaultSuccessUrl);

        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(result));
        writer.close();
    }
}
