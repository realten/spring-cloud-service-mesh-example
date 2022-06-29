package com.datasolution.msa.gateway.route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RouteLocatorRegistry {
    private final BeforeRoute beforeRoute;
    private final AfterRoute afterRoute;
    private final BetweenRoute betweenRoute;
    private final ABTestRoute abTestRoute;
    private final CookieRoute cookieRoute;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        //Before
        routes.route("before", beforeRoute.beforeRoute());
        //After
        routes.route("after", afterRoute.afterRoute());
        //Between
        routes.route("between", betweenRoute.betweenRoute());
        //Cookie
        routes.route("cookie", cookieRoute.cookieRoute());


        //AB TEST
        routes.route("microservice1", abTestRoute.abTest("microservice1"));
        routes.route("microservice2", abTestRoute.abTest("microservice2"));

        return routes.build();
    }
}
