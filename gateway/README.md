# Spring Cloud Gateway 

## Dependency Library 
```groovy
dependencies {
    /* Spring Cloud Gateway */
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
}
```

## Route 구성요소
* Route
  * 고유 ID, 목적지 URI, Predicate, Filter로 구성된 요소
  * Gateway로 요청된 URI의 조건이 참일 경우 매핑된 해당 경로로 매칭
* Predicate
  * HTTP 요청이 정의된 조건과 부합하는지 검사하는 요소
* Filter
  * 게이트웨이에서 받은 요청과 응답을 수정 가능하게 해주는 요소
* yaml로 구성 시 예제
  ```yaml
  spring:
    cloud:
      gateway:
        routes:
          - id: example
            uri: http://localhost:8081
            predicates:
              - Path=/api/**
            filters:
              - RewritePath=/api(?<segment>/?.*), /$\{segment}
  ```

## Predicate 종류
### After
* After는 지정된 날짜 이후에 발생하는 요청과 일치하는지 확인하는 조건이다.
* 예제 (소스 : [AfterRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/AfterRoute.java))
  ```java
  LocalDateTime dateTime = LocalDateTime.now().withHour(18).withMinute(0).withSecond(0).withNano(0);
  ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Asia/Seoul"));
  BooleanSpec booleanSpec = p.path("/api/route-sample/after")
            .and().after(zonedDateTime);
  ```
### Before
* Before는 지정된 날짜 이전에 발생하는 요청과 일치하는지 확인하는 조건이다.
* 예제 (소스 : [BeforeRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/BeforeRoute.java))
  ```java
  LocalDateTime dateTime = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
  ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Asia/Seoul"));
  BooleanSpec booleanSpec = p.path("/api/route-sample/before")
            .and().before(zonedDateTime);
  ```
### Between
* Between는 지정된 날짜 사이에 발생하는 요청과 일치하는지 확인하는 조건이다.
* 예제 (소스 : [BetweenRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/BetweenRoute.java))
  ```java
  LocalDateTime afterDateTime = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
  LocalDateTime beforeDateTime = LocalDateTime.now().withHour(18).withMinute(0).withSecond(0).withNano(0);
  ZonedDateTime afterZonedDateTime = ZonedDateTime.of(afterDateTime, ZoneId.of("Asia/Seoul"));
  ZonedDateTime beforeZonedDateTime = ZonedDateTime.of(beforeDateTime, ZoneId.of("Asia/Seoul"));
  BooleanSpec booleanSpec = p.path("/api/route-sample/between")
            .and().between(afterZonedDateTime, beforeZonedDateTime);
  ```
### Cookie
* Cookie는 쿠키 값이 정규식과 일치하는지 확인하는 조건이다.
* 예제 (소스 : [CookieRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/CookieRoute.java))
  ```java
  BooleanSpec booleanSpec = p.path("/api/route-sample/cookie")
            .and().cookie("cookie", "authinfo");
  ```
### Header
* Header는 헤더 값이 정규식과 일치하는지 확인하는 조건이다.
* 예제 (소스 : [HeaderRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/HeaderRoute.java))
  ```java
  BooleanSpec booleanSpec = p.path("/api/route-sample/header")
            .and().header(HttpHeaders.AUTHORIZATION, "headerInfo");
  ```
### Method
* Method는 Method 값이 일치하는지 확인하는 조건이다.
* 예제 (소스 : [MethodRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/MethodRoute.java))
  ```java
  BooleanSpec booleanSpec = p.path("/api/route-sample/method")
            .and().method(HttpMethod.POST);
  ```
### Path
* Path는 URI 값이 일치하는지 확인하는 조건이다.
* 예제 (소스 : [PathRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/PathRoute.java))
  ```java
  BooleanSpec booleanSpec = p.path("/api/route-sample/path");
  ```
### Query
* Query는 QueryString 값이 일치하는지 확인하는 조건이다.
* 예제 (소스 : [QueryRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/QueryRoute.java))
  ```java
  BooleanSpec booleanSpec = p.path("/api/route-sample/query")
            .and().query("param");
  ```
### RemoteAddr
* RemoteAddr는 요청 원격 주소가 일치하는지 확인하는 조건이다.
* 예제 (소스 : [RemoteAddrRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/RemoteAddrRoute.java))
  ```java
  BooleanSpec booleanSpec = p.path("/api/route-sample/remoteAddr")
            .and().remoteAddr("::1");
  ```
### Weight
* Weight는 가중치를 부여하는 조건이다.
* Weight는 그룹별로 계산되므로 Routing을 두 개 이상 설정해야 확인이 가능하다.
* 예제 (소스 : [WeightRoute](/src/main/java/com/datasolution/msa/gateway/route/sample/WeightRoute.java))
  ```java
  RouteLocatorBuilder.Builder routes = builder.routes();
  //Weight
  routes.route("weight1", weightRoute.weightRoute());
  routes.route("weight2", weightRoute.weightRoute());
  return routes.build();
  ```
  ```java
  BooleanSpec booleanSpec = p.path("/api/route-sample/weight")
                    .and().weight("weight", 5);
  ```

## Filter
* Filter는 Order 값에 따라 동작하는 순서가 정해져있으며, GlobalFilter와 GatewayFilter 인터페이스에 따라 적용범위가 달라진다.
  * GlobalFilter 인터페이스는 모든 Route에 조건부로 적용되는 필터이다.
  * GatewayFilter 인터페이스는 대상 Route에 적용되는 필터이다.

### GlobalFilter
* 다음은 GlobalFilter를 적용하여 구성하는 방법에 대해 설명한다.
  * 소스 예제
    * [PreGlobalFilter](/src/main/java/com/datasolution/msa/gateway/filter/PreGlobalFilter.java)
    * [PostGlobalFilter](/src/main/java/com/datasolution/msa/gateway/filter/PostGlobalFilter.java)
  * 소스 구성 방법
    ```java
    @Slf4j
    @Configuration
    public class P*GlobalFilter implements GlobalFilter, Ordered {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            //수행 로직 작성
            return chain.filter(exchange);
        }
    
        @Override
        public int getOrder() {
            return -1;    //적용할 순서 지정
        }
    }  
    ```
    * 클래스 생성 후 `GlobalFilter`, `Ordered`를 상속한다.
    * 상속 후 인터페이스 메소드를 오버라이드하여 역할에 맞게 로직을 작성한다.

### GatewayFilter
* 다음은 GatewayFilter를 적용하여 구성하는 방법에 대해 설명한다.
  * 소스 예제
    * [AfterRoute.java](/src/main/java/com/datasolution/msa/gateway/route/sample/AfterRoute.java)
  * 소스 구성 방법
    ```java
    public Function<PredicateSpec, Buildable<Route>> afterRoute() {
        return p -> {
            //...
            //filter 정의
            UriSpec filters = booleanSpec.filters(gatewayFilterSpecUriSpecFunction());
            //...
        }
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
    ```

## Timeout 설정
* Timeout 설정에는 **Global 설정**과 **각 Route 설정**이 있다.
### Global 설정
* [GlobalTimeoutConfiguration.java](/src/main/java/com/datasolution/msa/gateway/timeout/GlobalTimeoutConfiguration.java)
  ```java
  @Component
  public class GlobalTimeoutConfiguration {
      public GlobalTimeoutConfiguration(@Qualifier("httpClientProperties") HttpClientProperties httpClientProperties) {
          int time = 1000 * 60;
          httpClientProperties.setConnectTimeout(time);
          httpClientProperties.setResponseTimeout(Duration.ofMillis(time));
      }
  }
  ```
  * `org.springframework.cloud.gateway.config.HttpClientProperties` Bean을 가져온다.
  * Timeout 시간은 1분(1000(1초) * 60)으로 상황에 맞게 설정한다.
  * ConnectionTimeout은 요청 서버와 커넥션 맺는데 기다리는 시간이다.
  * ResponseTimeout은 HTTP 요청/응답 시간에 대한 Timout으로 ConnectionTimeout + 응답까지의 시간이다.
* yaml 설정 방법
  ```yaml
  spring:
    cloud:
      gateway:
        httpclient:
          connect-timeout: 60000
          response-timeout: 60s
  ```

### 각 Route 설정
* [TimeoutRoute.java](/src/main/java/com/datasolution/msa/gateway/route/TimeoutRoute.java)
  ```java
  private Function<GatewayFilterSpec, UriSpec> gatewayFilterSpecUriSpecFunction() {
      return gatewayFilterSpecUriSpecFunction -> {
          gatewayFilterSpecUriSpecFunction.stripPrefix(1).filter(gatewayFilter())
                  // Timeout 설정 : milliseconds 단위
                  .metadata(CONNECT_TIMEOUT_ATTR, 2000);
                  .metadata(RESPONSE_TIMEOUT_ATTR, 2000)
  
          return gatewayFilterSpecUriSpecFunction;
      };
  }
  ```
  * Timeout설정은 Filter에서 설정하며 metadata에 CONNECT_TIMEOUT_ATTR과 RESPONSE_TIMEOUT_ATTR에 대해 설정한다.
* yaml 설정 방법
  ```yaml
  spring:
    cloud:
      gateway:
        routes:
          id: timeoutRoute
          uri: http://localhost:8080
          predicates:
          - name: Path
            args:
              pattern: /delay/{timeout}
          metadata:
            response-timeout: 2000
            connect-timeout: 2000
  ```

## Route 동적 등록 방법
* 동적 등록
```http request
###
POST http://localhost:8000/actuator/gateway/routes/asnyc-route
Content-Type: application/json

{
    "route_id": "asnyc-route",
    "predicates": ["Method=[POST]", "Path=/api/route-sample/asnyc-route"],
    "filters": ["StripPrefix=1"],
    "uri"; "lb://microservice1",
    "order": 0
}
```
* 등록 후 Refresh
```http request
###
POST http://localhost:8000/actuator/gateway/refresh
```
* 등록 된 Route 삭제
```http request
###
DELETE http://localhost:8000/actuator/gateway/routes/async-route
```

## 참조
* Spring Cloud Gateway Docs
  * https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/