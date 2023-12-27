package com.example.jwtpractice231222.config;

import com.example.jwtpractice231222.config.filter.JwtFilter;
import com.example.jwtpractice231222.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig{
    private final MemberService memberService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /*
        jwt token을 사용하면 session을 사용해야 할까?

    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/jwt/*").permitAll()
                    .antMatchers("/member/*").permitAll()
                    .antMatchers("/test/*").hasRole("USER")
                    .anyRequest().authenticated();

            httpSecurity.addFilterBefore(new JwtFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class);
            // UsernamePasswordAuthenticationFilter <- 이 필터가 실행되기 전에 내 필터를 끼워 넣겠다.

            httpSecurity.formLogin().disable();

            // 세션 설정
            // Stateless는 내가 생성도 안하고 너가 가지고 있어도 나는 사용하지 않을 거야라는 의미.
            httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
