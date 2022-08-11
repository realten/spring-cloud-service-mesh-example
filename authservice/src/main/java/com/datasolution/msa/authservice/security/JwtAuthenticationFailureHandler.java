package com.datasolution.msa.authservice.security;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.print.attribute.standard.Media;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private String defaultFailureUrl = "/login?error";

    public JwtAuthenticationFailureHandler() {

    }

    public JwtAuthenticationFailureHandler(String defaultFailureUrl) {
        if(defaultFailureUrl != null) {
            this.defaultFailureUrl = defaultFailureUrl;
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> result = new HashMap<>();
        result.put("status", false);
        result.put("url", defaultFailureUrl);

        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(result));
        writer.close();
    }
}
