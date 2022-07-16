package com.datasolution.msa.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String homeView() {
        return "home";
    }
}
