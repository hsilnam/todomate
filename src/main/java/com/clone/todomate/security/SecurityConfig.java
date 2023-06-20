package com.clone.todomate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
//@EnableWebSecurity
public class SecurityConfig {
    private final String[] permitAllPaths = {"/member/login", "/member/join"};

    private final TokenProvider tokenProvider;

    // 암호화 모듈 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 쿠키, 세션을 사용하지 않기 때문에 세션 정책을 STATELESS로, CSRF 비활성화 설정
                .csrf().disable()
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .headers(headers -> headers.frameOptions().sameOrigin()) // QUESTION: h2 콘솔 사용을 위한 설정?

                // 권한없이도 접근 허용할 url 설정 (white list)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(permitAllPaths).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .cors(cors -> {//CORS 설정
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration corsConfig = new CorsConfiguration();
                        corsConfig.setAllowCredentials(true);
                        corsConfig.setAllowedOrigins(List.of("*"));
                        corsConfig.addAllowedHeader("*");
                        corsConfig.setAllowedMethods(List.of("*"));
                        return corsConfig;
                    };
                    cors.configurationSource(source);
                })
                .exceptionHandling(exception -> {
                    exception
                            .accessDeniedHandler(new JwtAccessDeniedHandler())
                            .authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                })
                .build();
    }
}
