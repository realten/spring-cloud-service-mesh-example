package com.datasolution.msa.authservice.security;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationManager implements AuthenticationManager {
    @Autowired
    private SqlSessionTemplate dao;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("principal - {}, credential - {}", authentication.getPrincipal(), authentication.getCredentials());
        UserVo userVo = dao.selectOne("com.datasolution.msa.authservice.selectUserInfo", authentication.getPrincipal());
        if(userVo == null) {
            throw new UsernameNotFoundException("User not Found!");
        }
        log.info("UserVo - {}", userVo);
        if(!userVo.getPassword().equals(authentication.getCredentials())) {
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userVo.getUsername(), userVo.getPassword(), userVo.getAuthorities());
    }
}
