package com.datasolution.msa.microservice2.controller;

import com.datasolution.msa.microservice2.utils.LocalDateTimeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/header")
    public Map<String, Object> headerRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - headerRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/query")
    public Map<String, Object> queryRoute(@RequestParam(value = "microservice")String microservice) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - queryRoute");
        map.put("value", microservice);
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/weight")
    public Map<String, Object> abtestRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice2 - abtestRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }




}
