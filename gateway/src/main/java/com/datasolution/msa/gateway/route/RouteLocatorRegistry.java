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
    private final ABTestRoute abTestRoute;

    private final AfterRoute afterRoute;
    private final BeforeRoute beforeRoute;
    private final BetweenRoute betweenRoute;
    private final CookieRoute cookieRoute;
    private final HeaderRoute headerRoute;
    private final QueryRoute queryRoute;
    private final WeightRoute weightRoute;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        //After
        routes.route("after", afterRoute.afterRoute());
        //Before
        routes.route("before", beforeRoute.beforeRoute());
        //Between
        routes.route("between", betweenRoute.betweenRoute());
        //Cookie
        routes.route("cookie", cookieRoute.cookieRoute());
        //Header
        routes.route("header", headerRoute.headerRoute());
        //Query
        routes.route("query", queryRoute.queryRoute());
        //Weight
        routes.route("weight1", weightRoute.weightRoute());
        routes.route("weight2", weightRoute.weightRoute());


        //AB TEST
        routes.route("microservice1", abTestRoute.abTest("microservice1"));
        routes.route("microservice2", abTestRoute.abTest("microservice2"));

        return routes.build();
    }
}
