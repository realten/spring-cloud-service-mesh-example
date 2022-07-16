package com.datasolution.msa.gateway.route.sample;

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
public class PathRoute {
    /**
     * Path Route<br />
     * <br />
     * /api/route-sample/path 으로 들어오는 경우<br />
     * <br />
     * Filter는 gatewayFilter 적용<br />
     * <br />
     * URI : LoadBalancing to microservice1 application<br />
     *
     * @return
     */
    public Function<PredicateSpec, Buildable<Route>> pathRoute() {
        Map<String, String> map = new HashMap<>();
        return p -> {
            // 조건절 정의
            /* path(matchTrailingSlsh, patterns)
               - matchTrailingSlsh : 후행 슬래시에 대한 설정 값
                 - true : /route-sample/path == /route-sample/path/
                 - false : /route-sample/path != /route-sample/path/
               - patterns : URI 경로를 확인할 패턴
            */
            BooleanSpec booleanSpec = p.path("/api/route-sample/path");

            //filter 정의
            UriSpec filters = booleanSpec.filters(gatewayFilterSpecUriSpecFunction());

            //URI 정의
            String uri = "";
            uri = "lb://microservice1";
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
