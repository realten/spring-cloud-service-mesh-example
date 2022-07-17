package com.datasolution.msa.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/route-sample")
public class RouteSampleController {
    @GetMapping("")
    public String routeSampleView() {
        return "sample/route-sample";
    }
}
