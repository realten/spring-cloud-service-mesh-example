package com.datasolution.msa.microservice1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @GetMapping("/getShoesShopList")
    public List<String> getShoesShopList() {
        List<String> list = new ArrayList<>();
        list.add("Nike");
        list.add("NewBalance");
        list.add("Jordan");
        return list;
    }
}
