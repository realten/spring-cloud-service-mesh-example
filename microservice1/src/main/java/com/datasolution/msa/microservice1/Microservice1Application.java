package com.datasolution.msa.microservice1;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {MybatisAutoConfiguration.class}
)
public class Microservice1Application {
    public static void main(String[] args) {
        SpringApplication.run(Microservice1Application.class, args);
    }
}
