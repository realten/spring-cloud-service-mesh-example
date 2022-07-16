package com.datasolution.msa.microservice1.controller;

import com.datasolution.msa.microservice1.utils.LocalDateTimeUtils;
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
        map.put("message", "microservice1 - beforeRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/after")
    public Map<String, Object> afterRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - afterRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/between")
    public Map<String, Object> betweenRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - betweenRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/abtest")
    public Map<String, Object> abtestRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - abtestRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/cookie")
    public Map<String, Object> cookieRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - cookieRoute");
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

    @GetMapping("/path")
    public Map<String, Object> pathRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - pathRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/query")
    public Map<String, Object> queryRoute(@RequestParam(value = "param")String param) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - queryRoute");
        map.put("paramValue", param);
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/remoteAddr")
    public Map<String, Object> remoteAddrRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - remoteAddrRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }

    @GetMapping("/test")
    public Map<String, Object> testRoute() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "microservice1 - testRoute");
        map.put("exec-time", LocalDateTimeUtils.now());
        return map;
    }


}
