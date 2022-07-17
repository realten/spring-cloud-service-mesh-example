package com.datasolution.msa.gateway.timeout;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 글로벌 타임아웃 설정
 * spring:
 *   cloud:
 *     gateway:
 *       httpclient:
 *         connect-timeout: 60000
 *         response-timeout: 60s
 */
@Component
public class GlobalTimeoutConfiguration {
    public GlobalTimeoutConfiguration(@Qualifier("httpClientProperties") HttpClientProperties httpClientProperties) {
        int time = 1000 * 60;
        httpClientProperties.setConnectTimeout(time);
        httpClientProperties.setResponseTimeout(Duration.ofMillis(time));
    }
}
