package com.datasolution.msa.foo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foo/version-test")
@Slf4j
public class ABTestController {
    @GetMapping("/v1")
    public String getVersion1() {
        return "v1";

    }
    @GetMapping("/v2")
    public String getVersion2() {
        return "v2";
    }


}
