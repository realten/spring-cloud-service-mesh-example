package com.datasolution.msa.microservice1.controller;

import com.datasolution.msa.microservice1.security.JwtUtil;
import com.datasolution.msa.microservice1.vo.MemberVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public String login(@RequestBody MemberVo memberVo) {
        JwtUtil jwtUtil = new JwtUtil();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(memberVo, Map.class);
        return jwtUtil.createToken(map);
    }
}
