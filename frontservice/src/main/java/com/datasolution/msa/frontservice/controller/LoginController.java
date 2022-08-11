package com.datasolution.msa.frontservice.controller;

import com.datasolution.msa.frontservice.vo.LoginVo;
import com.datasolution.msa.frontservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
public class LoginController {
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(HttpServletRequest request, LoginVo loginVo) {
        String resultMessage = null;
        StringBuilder url = new StringBuilder("redirect:");

        try {
            resultMessage = WebClient.create()
                    .post()
                    .uri("http://localhost:8000/api/auth/login")
                    .bodyValue(loginVo)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<String>() {}).block();
        } catch (Exception e) {
            e.printStackTrace();
            url.append("/login");
        }

        Map<String, Object> resultMap = new GsonJsonParser().parseMap(resultMessage);
        boolean resultStatus = (boolean) resultMap.get("status");
        String resultUrl = (String) resultMap.get("url");
        url.append(resultUrl);
        if(resultStatus) {
            HttpSession session = request.getSession(true);
            UserVo userVo = new UserVo();
            userVo.setLoginYn(true);
            String token = (String) resultMap.get("jwt");
            userVo.setToken(token);
            userVo.setUsername(loginVo.getUsername());
            session.setAttribute("userVo", userVo);
        }
        return url.toString();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

}
