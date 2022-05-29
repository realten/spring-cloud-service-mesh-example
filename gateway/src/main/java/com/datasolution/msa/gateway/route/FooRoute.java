package com.datasolution.msa.gateway.route;

import com.datasolution.msa.gateway.filter.FooFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@Slf4j
public class FooRoute {
    @Bean
    public RouteLocator fooRouter(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        routes.route("version-a", getVersionTest());
        routes.route("version-b", getVersionTest());

        return routes.build();
    }

    /**
     * AB Test Route
     *
     * /api/foo/version-test/**으로 들어오는 경우
     * version-test라는 그룹의 가중치 5
     *
     * Filter는 FooFilter 적용
     *
     * URI : foo application LoadBalancing
     *
     * @return
     */
    private Function<PredicateSpec, Buildable<Route>> getVersionTest() {
        return p -> {

            // 조건절 정의
            BooleanSpec booleanSpec = p.path("/api/foo/version-test/**").and()
                    .weight("version-test", 5);

            //filter 정의
            UriSpec filters = booleanSpec.filters(f -> f.filter(new FooFilter().apply(config -> {
                config.setRegexp("/api/foo(?<segment>/?.*)");
                config.setReplacement("/foo${segment}");
            })));

            //URI 정의
            Buildable<Route> uri = filters.uri("lb://foo");

            return uri;
        };
    }
}
