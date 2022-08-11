package com.datasolution.msa.gateway.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Configuration
public class AuthRoute {
    /**
     * Auth Route<br />
     * <br />
     * /api/auth/** 으로 들어오는 경우<br />
     * <br />
     * Filter는 gatewayFilter 적용<br />
     * - Response Time out, Connect Time out 설정 : 2초<br />
     * <br />
     * URI : LoadBalancing to microservice1 application<br />
     *
     * @return
     */
    public Function<PredicateSpec, Buildable<Route>> authRoute() {
        Map<String, String> map = new HashMap<>();
        return p -> {
            // 조건절 정의
            BooleanSpec booleanSpec = p.path("/api/auth/**");

            //filter 정의
            UriSpec filters = booleanSpec.filters(gatewayFilterSpecUriSpecFunction());

            //URI 정의
            String uri = "";
            uri = "lb://authservice";
            Buildable<Route> routeBuildable = filters.uri(uri);

            return routeBuildable;
        };
    }

    private Function<GatewayFilterSpec, UriSpec> gatewayFilterSpecUriSpecFunction() {
        return gatewayFilterSpecUriSpecFunction -> {
            gatewayFilterSpecUriSpecFunction.stripPrefix(1).filter(gatewayFilter());
            return gatewayFilterSpecUriSpecFunction;
        };
    }

    private GatewayFilter gatewayFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            log.info("route-id - {}, path - {}", route.getId(), path);
            String rewritePath = path;
            log.info("rewritePath - {}", rewritePath);
            ServerHttpRequest request = exchange.getRequest().mutate().path(rewritePath).build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }
}
