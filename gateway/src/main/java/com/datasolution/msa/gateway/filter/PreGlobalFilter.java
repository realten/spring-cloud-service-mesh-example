package com.datasolution.msa.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class PreGlobalFilter implements GlobalFilter, Ordered {
    private List<String> logList = new ArrayList<>();
    private int maxLength = 0;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logList = new ArrayList<>();
        maxLength = 0;
        addList("preGlobalFilter");
        addList("Request Id - " + request.getId());
        addList("Request URI - " + request.getURI());
        addList("Request Method - " + request.getMethod());
        printLog();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private void addList(String str) {
        if(str.length() > maxLength) {
            maxLength = str.length();
        }
        logList.add(str);
    }

    private void printLog() {
        logList.stream().findFirst().ifPresent(e -> log.info("{}", "┌─ " + StringUtils.rightPad(e + " ", maxLength, "─") + "┐"));
        logList.stream().skip(1).forEach(e -> log.info("{}", String.format("│ %-" + maxLength + "s │", e)));
        log.info("{}", "└" + StringUtils.rightPad("", maxLength + 2, "─") + "┘");
    }
}
