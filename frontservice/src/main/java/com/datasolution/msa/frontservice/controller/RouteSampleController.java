package com.datasolution.msa.frontservice.controller;

import com.datasolution.msa.frontservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/route-sample")
public class RouteSampleController {
    @GetMapping("")
    public String routeSampleView(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        UserVo userVo = (UserVo) session.getAttribute("userVo");
        if(userVo == null) {
            userVo = new UserVo();
            userVo.setLoginYn(false);
        }
        modelMap.addAttribute("userVo", userVo);
        log.info("userVo - {}", userVo);
        return "sample/route-sample";
    }
}
