package com.datasolution.msa.gateway.route;

import io.github.resilience4j.circuitbreaker.configure.CircuitBreakerConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Configuration
public class ABTestRoute {
    /**
     * AB Test Route<br />
     * <br />
     * /api/version-check/**으로 들어오는 경우<br />
     * abtest라는 그룹의 가중치 5<br />
     * <br />
     * Filter는 gatewayFilter 적용<br />
     * <br />
     * URI : LoadBalancing to microservice1 application, microservice2 application<br />
     *
     * @return
     */
    public Function<PredicateSpec, Buildable<Route>> abTest(String routeId) {
        return p -> {
            // 조건절 정의
            BooleanSpec booleanSpec = p.path("/api/version-check/**").and()
                    .weight("abtest", 5);

            //filter 정의
            UriSpec filters = booleanSpec.filters(gatewayFilterSpecUriSpecFunction());

            //URI 정의
            String uri = "";
            if("microservice1".equals(routeId)) {
                uri = "lb://microservice1";
            } else if("microservice2".equals(routeId)) {
                uri = "lb://microservice2";
            }
            Buildable<Route> routeBuildable = filters.uri(uri);

            return routeBuildable;
        };
    }

    private Function<GatewayFilterSpec, UriSpec> gatewayFilterSpecUriSpecFunction() {
        return gatewayFilterSpecUriSpecFunction -> {
            gatewayFilterSpecUriSpecFunction.stripPrefix(1).filter(gatewayFilter())
                    .circuitBreaker(springCloudCircuitBreakerFilterFactoryConfigConsumer());
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

    public Consumer<SpringCloudCircuitBreakerFilterFactory.Config> springCloudCircuitBreakerFilterFactoryConfigConsumer() {
        return springCloudCircuitBreakerFilterFactoryConfig -> {
            SpringCloudCircuitBreakerFilterFactory.Config config = springCloudCircuitBreakerFilterFactoryConfig;
            config.setName("customCircuitBraeker")
                    .setFallbackUri("forward:/actuator/health");
            List<HttpStatus> httpStatusList = new ArrayList<>();
            httpStatusList.add(HttpStatus.NOT_FOUND);
            for(HttpStatus httpStatus : httpStatusList) {
                config.addStatusCode(String.valueOf(httpStatus.value()));
            }
        };
    }
}
