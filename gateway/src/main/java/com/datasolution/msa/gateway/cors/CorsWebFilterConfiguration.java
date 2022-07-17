package com.datasolution.msa.gateway.cors;

import org.apache.http.HttpClientConnection;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.time.Duration;

@Configuration
public class CorsWebFilterConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        /*
         * Credentials 값 전송 여부
         * true - cookie 값 전송
         * false - cookie 값 미전송
         */
        corsConfiguration.setAllowCredentials(true);

        //urlBasedCorsConfigurationSource.registerCorsConfiguration("/{segment:(?!exclude).*}", corsConfiguration);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}

/*
Jquery - Ajax에서 쿠키 값 전송 시 필요 옵션 withCredentials: true
$.ajax({
    url: "http://localhost:8080/route/cors",
    method: "GET",
    dataType: "text",
    xhrFields: {
        withCredentials: true
    },
    success: function(result) {
        $(".result").text(result);
    }
})
*/
