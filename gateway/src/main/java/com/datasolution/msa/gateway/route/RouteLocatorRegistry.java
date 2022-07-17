package com.datasolution.msa.gateway.route;

import com.datasolution.msa.gateway.route.sample.*;
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
    // Sample Route
    private final AfterRoute afterRoute;
    private final BeforeRoute beforeRoute;
    private final BetweenRoute betweenRoute;
    private final CookieRoute cookieRoute;
    private final HeaderRoute headerRoute;
    private final MethodRoute methodRoute;
    private final PathRoute pathRoute;
    private final QueryRoute queryRoute;
    private final RemoteAddrRoute remoteAddrRoute;
    private final WeightRoute weightRoute;
    private final TimeoutRoute timeoutRoute;


    private final ABTestRoute abTestRoute;


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
        //Method
        routes.route("method", methodRoute.methodRoute());
        //Path
        routes.route("path", pathRoute.pathRoute());
        //Query
        routes.route("query", queryRoute.queryRoute());
        //RemoteAddr
        routes.route("remoteAddr", remoteAddrRoute.remoteAddrRoute());
        //Weight
        routes.route("weight1", weightRoute.weightRoute());
        routes.route("weight2", weightRoute.weightRoute());
        //Timeout
        routes.route("timeout", timeoutRoute.timeoutRoute());

        //AB TEST
        routes.route("microservice1", abTestRoute.abTest("microservice1"));
        routes.route("microservice2", abTestRoute.abTest("microservice2"));

        return routes.build();
    }
}
