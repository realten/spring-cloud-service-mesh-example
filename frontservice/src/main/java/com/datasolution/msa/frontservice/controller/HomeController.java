package com.datasolution.msa.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public String homeView(){
        return "home";
    }

    @GetMapping("/style-sample")
    public String styleSampleView() {
        return "sample/style-sample";
    }

}
