package com.datasolution.msa.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Custom GatewayFilter Factory 구현<br/>
 * <br/>
 * GatewayFilter를 작성하려면 GatewayFilterFactory를 구현해야 합니다.<br/>
 * AbstractGatewayFilterFactory라는 추상클래스를 확장할 수 있습니다.<br/>
 * 참고 : https://cloud.spring.io/spring-cloud-gateway/reference/html/#writing-custom-gatewayfilter-factories<br/>
 */
@Slf4j
public class FooFilter extends AbstractGatewayFilterFactory<FooFilter.Config> {
    public FooFilter() {
        super(Config.class);
    }

    /**
     * /foo/version-test/version으로 호출 시
     * route-id가 version-a인 경우 - v1로 route
     * route-id가 version-b인 경우 - v2로 route
     *
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String newPath = exchange.getRequest().getURI().getPath().replaceAll(config.getRegexp(), config.getReplacement());
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            ServerHttpRequest req = exchange.getRequest();
            log.info("newPath - {}", newPath);
            log.info("route-id - {}", route.getId());

            if(newPath.equals("/foo/version-test/version")) {
                if(route.getId().equals("version-a")) {
                    newPath = "/foo/version-test/v1";
                } else if(route.getId().equals("version-b")) {
                    newPath = "/foo/version-test/v2";
                }
            }
            log.info("newPath - {}", newPath);
            ServerHttpRequest request = exchange.getRequest().mutate().path(newPath).build();
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    @Data
    public static class Config {
        private String regexp;
        private String replacement;
    }
}


