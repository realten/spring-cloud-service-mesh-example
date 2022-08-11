package com.datasolution.msa.authservice.security;

import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 5.7.0-M2부터 사용자가 구성 요소 기반 보안 구성으로 이동하도록 권장하여 WebSecurityConfigurerAdapter가 Deprecated됨
 * 참고 : https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * 정적 리소스 제외 로직 구현<br>
     * └─ nginx 분리로 인해 미사용
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //정적 리소스 spring security 대상 제외
        return (web) -> web.ignoring().antMatchers("/templates/**", "/static/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* CORS 비활성화 */
        http.cors().disable();

        /* CSRF 공격 방지 비활성화 */
        http.csrf().disable();

        /* RememberMe 비활성화
         * 세션 길이를 늘려주는 옵션으로 form 로그인 방식에서 체크 시 default 세션이 30분에서 2주로 변경
         * 브라우저를 껏다 다시 켜도 유지됨
         */
        http.rememberMe().disable();

        /* Session 정책 Stateless */
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /* JWT Filter */
        http.addFilterBefore(jwtAuthenticationProcessingFilter()
                , UsernamePasswordAuthenticationFilter.class);

        /* 인증 요청 설정 */
        http.authorizeHttpRequests()
                // /와 /auth 로 들어오는 경로는 모든 ROLE 허용
                .antMatchers("/", "/auth", "/test").permitAll()
                // /admin으로 들어오는 경로는 ADMIN ROLE을 가진 유저에게만 허용
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                ;

        /* 로그인 설정 */
        /* form 인증 비활성화 */
        http.formLogin().disable()
//                .loginPage("/auth/login")   // 로그인 페이지 경로는 /auth/login
//                .usernameParameter("id")    // 로그인 시 아이디 컬럼명은 id, default : username
//                .defaultSuccessUrl("/")     // 로그인 성공 시 redirect url 주소
                ;
        /* basic 인증 비활성화 */
        http.httpBasic().disable()
                ;

        /* 로그아웃 설정 */
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/")
                ;

        return http.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter filter = new JwtAuthenticationProcessingFilter();
        filter.setAuthenticationManager(jwtAuthenticationManager());
        filter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
        filter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        return filter;
    }

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager() {
        return new JwtAuthenticationManager();
    }

//    @Bean
//    public UserDetailsService inMemoryUserDetailsService() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        User.UserBuilder users = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2$16$7AWWNhFKJi.w8ntJbrSoGerYCiJyZWEnweMkFbShz2tr8BXpN3Fti")
//                .roles("ADMIN");
//        inMemoryUserDetailsManager.createUser(users.build());
//        return inMemoryUserDetailsManager;
//    }
}
