package com.datasolution.msa.microservice2.controller;

import com.datasolution.msa.microservice2.utils.LocalDateTimeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/route-sample")
public class RouteSampleController {
    @GetMapping("/before")
    public Map<String, Object> beforeRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice2 - beforeRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/after")
    public Map<String, Object> afterRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice2 - afterRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/between")
    public Map<String, Object> betweenRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice2 - betweenRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/abtest")
    public Map<String, Object> abtestRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice2 - abtestRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }


}
