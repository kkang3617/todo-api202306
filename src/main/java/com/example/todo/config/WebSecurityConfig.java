package com.example.todo.config;

import com.example.todo.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

//@Configuration //설정 클래스 용도로 사용하도록 스프링에 등록하는 아노테이션
@EnableWebSecurity // 시큐리티 설정 파일로 사용할 클래스 선언.
@RequiredArgsConstructor
//자동 권한검사를 수행하기 위한 결정.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //시큐리티 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Security 모듈이 기본적으로 제공하는 보안 정책 해제.
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //어떤 요청에서 인증을 안 할 것인지 설정, 언제 할 것인지 설정
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/api/auth/promote")
                .authenticated()
                .antMatchers("/","/api/auth/**").permitAll() // localhost:8181
//                .antMatchers(HttpMethod.POST, "/api/todos").hasRole("ADMIN")
                .anyRequest().authenticated(); // 나머지는 다 인증을 받아야한다.
        
        //토큰 인증 필터 연결
        http.addFilterAfter(
                jwtAuthFilter,
                CorsFilter.class // import 주의: 스프링 꺼로!!
        );


        return http.build();
    }

}







