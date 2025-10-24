package com.example.spring_boot.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BotKeyFilter extends OncePerRequestFilter {

    private String expectedToken;

    public BotKeyFilter(String token) {
        this.expectedToken = token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/bot")) {
            String key = request.getHeader("X-BOT-KEY");
            if (!key.equals(expectedToken)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("telegram-bot", null,
                    List.of(new SimpleGrantedAuthority("ROLE_BOT")));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

}
