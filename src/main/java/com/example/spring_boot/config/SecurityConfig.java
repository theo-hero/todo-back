package com.example.spring_boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.spring_boot.jwt.AuthEntrypointJwt;
import com.example.spring_boot.jwt.AuthTokenFilter;
import com.example.spring_boot.service.AppUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Value("${app.tg-bot.token}")
        private String botApiToken;

        @Autowired
        private AuthEntrypointJwt unauthorizedHandler;

        @Bean
        @Order(1)
        SecurityFilterChain botChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/bot/**")
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole("BOT"))
                                .addFilterBefore(new BotKeyFilter(botApiToken), UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        @Order(2)
        SecurityFilterChain appChain(HttpSecurity http, AuthTokenFilter authTokenFilter)
                        throws Exception {
                http.cors(cors -> {
                });
                http.logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessHandler((request, response, authentication) -> {
                                        ResponseCookie removeUsername = ResponseCookie.from("username", "")
                                                        .maxAge(0)
                                                        .path("/")
                                                        .sameSite("Lax")
                                                        .build();

                                        ResponseCookie removeJwt = ResponseCookie.from("jwt", "")
                                                        .maxAge(0)
                                                        .httpOnly(true)
                                                        .secure(false)
                                                        .path("/")
                                                        .sameSite("Lax")
                                                        .build();

                                        response.setHeader(HttpHeaders.SET_COOKIE, removeJwt.toString());
                                        response.addHeader(HttpHeaders.SET_COOKIE, removeUsername.toString());
                                        response.setStatus(HttpServletResponse.SC_OK);
                                }));
                http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers("/test/**", "/signin", "/signup/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .anyRequest().authenticated());
                http.sessionManagement(
                                session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS));
                http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
                http.headers(headers -> headers
                                .frameOptions(frameOptions -> frameOptions
                                                .sameOrigin()));
                http.csrf(csrf -> csrf.disable());
                http.addFilterBefore(authTokenFilter,
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
                config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("Content-Type", "Authorization"));
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);

                var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }

        @Bean
        DaoAuthenticationProvider authenticationProvider(
                        AppUserDetailsService uds, PasswordEncoder encoder) {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider(uds);
                provider.setPasswordEncoder(encoder);
                return provider;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
