package com.datasolution.msa.gateway.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;

@Slf4j
@Configuration
public class BetweenRoute {
    /**
     * Between Route<br />
     * <br />
     * 9시부터 18시까지 /api/*&#47;route-sample/between 으로 들어오는 경우<br />
     * <br />
     * Filter는 gatewayFilter 적용<br />
     * <br />
     * URI : LoadBalancing to microservice1 application<br />
     *
     * @return
     */
    public Function<PredicateSpec, Buildable<Route>> betweenRoute() {
        return p -> {
            // 조건절 정의
            LocalDateTime afterDateTime = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime beforeDateTime = LocalDateTime.now().withHour(18).withMinute(0).withSecond(0).withNano(0);
            ZonedDateTime afterZonedDateTime = ZonedDateTime.of(afterDateTime, ZoneId.of("Asia/Seoul"));
            ZonedDateTime beforeZonedDateTime = ZonedDateTime.of(beforeDateTime, ZoneId.of("Asia/Seoul"));
            BooleanSpec booleanSpec = p.path("/api/route-sample/between").and()
                    .after(afterZonedDateTime).and()
                    .before(beforeZonedDateTime);

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
