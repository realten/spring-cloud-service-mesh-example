package com.datasolution.msa.gateway.filter;

import com.datasolution.msa.gateway.security.JwtUtil;
import com.datasolution.msa.gateway.util.PrintUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        PrintUtil.printLog(logList, maxLength);

        if(!request.getURI().getPath().startsWith("/api/auth") && !this.checkAuthentication(request)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean checkAuthentication(ServerHttpRequest request) {
        List<String> authorizations = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authorizations)) {
            return false;
        }
        try {
            String token = authorizations.stream().findFirst().get();
            JwtUtil.verifyToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    private void addList(String str) {
        if(str.length() > maxLength) {
            maxLength = str.length();
        }
        logList.add(str);
    }
}
