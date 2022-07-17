package com.datasolution.msa.gateway.route.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.function.Function;

@Configuration
@Slf4j
public class WeightRoute {
    /** ROUTE ID */
    private String routeId;

    /**
     * weight Route<br />
     * <br />
     * /api/route-sample/weight 으로 들어오는 경우<br />
     * weight라는 그룹의 가중치 5<br />
     * <br />
     * Filter는 gatewayFilter 적용<br />
     * <br />
     * URI<br />
     *  - RouteId가 weight1 인 경우 LoadBalancing to microservice1 application<br />
     *  - RouteId가 weight2 인 경우 LoadBalancing to microservice2 application<br />
     *
     * @return
     */
    public Function<PredicateSpec, Buildable<Route>> weightRoute() {

        return p -> {
            // 조건절 정의
            BooleanSpec booleanSpec = p.path("/api/route-sample/weight")
                    .and().weight("weight", 5);

            //filter 정의
            UriSpec filters = booleanSpec.filters(gatewayFilterSpecUriSpecFunction());

            //URI 정의
            String uri = "lb://microservice1";
            if ("weight2".equals(routeId)) {
                uri = "lb://microservice2";
            }
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
            routeId = route.getId();
            log.info("route-id - {}, path - {}", route.getId(), path);
            String rewritePath = path;
            log.info("rewritePath - {}", rewritePath);
            ServerHttpRequest request = exchange.getRequest().mutate().path(rewritePath).build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }
}
