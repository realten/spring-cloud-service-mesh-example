package com.datasolution.msa.microservice2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/version-check")
@RestController
public class VersionCheckController {
    @GetMapping("/getVersion")
    public Map<String, Object> getVersion(@Value("${spring.application.name}") String applicationName){
        Map<String, Object> map = new HashMap<>();

        map.put("applicationName", applicationName);

        LocalDateTime localDateTime = LocalDateTime.now();
        map.put("now", localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        map.put("version", "v2");
        return map;
    }
}
