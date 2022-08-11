package com.datasolution.msa.authservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_FILTER_PROCESSES_URL = "/auth/login";

    public JwtAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher(DEFAULT_FILTER_PROCESSES_URL, HttpMethod.POST.toString()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        // POST Method 에서만 접근하도록 예외처리
        if(!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: : " + request.getMethod());
        }

        // 입력받은 파라미터 값을 Json 형식으로 파싱
        GsonJsonParser gsonJsonParser = new GsonJsonParser();
        Map<String, Object> map = gsonJsonParser.parseMap(request.getReader().lines().collect(Collectors.joining()));
//        String username = (String) request.getParameter("username");
//        String password = (String) request.getParameter("password");
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        log.info("username - {}, password - {}", username, password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(token);
    }
}
