package com.example.spring_boot.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot.dto.LoginRequest;
import com.example.spring_boot.dto.LoginResponse;
import com.example.spring_boot.jwt.JwtUtils;
import com.example.spring_boot.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsManager userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.app.jwtExpirationMs}")
    private String expStr;

    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Integer.parseInt(expStr) / 1000)
                .sameSite("Lax")
                .build();

        ResponseCookie username = ResponseCookie.from("username", loginRequest.getUsername())
                .path("/")
                .sameSite("Lax")
                .build();

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, username.toString())
                .body(response);
    }

    @GetMapping("/test")
    public String sayHello() {
        return "HEY! Can see without authentication";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        return authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody LoginRequest loginRequest) {
        UserDetails user = User
                .withUsername(loginRequest.getUsername())
                .password(passwordEncoder.encode(loginRequest.getPassword()))
                .roles("USER")
                .build();

        userDetailsService.createUser(user);
        return authenticateUser(loginRequest);
    }

}
