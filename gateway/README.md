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
* 구성 예제
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