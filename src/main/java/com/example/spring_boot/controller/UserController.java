package com.example.spring_boot.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot.dto.AuthResult;
import com.example.spring_boot.dto.LoginRequest;
import com.example.spring_boot.service.AuthService;
import com.example.spring_boot.service.EmailService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @GetMapping("/test")
    public String sayHello() {
        return "HEY! Can see without authentication";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        AuthResult authResult = authService.signin(loginRequest);

        return ResponseEntity.ok()
                .headers(h -> authResult.cookies().forEach(c -> h.add(HttpHeaders.SET_COOKIE, c.toString())))
                .body(authResult.body());

    }

    @PostMapping("/signup/confirmation_code")
    public ResponseEntity<?> sendConfirmationCode(@RequestBody LoginRequest loginRequest) {
        authService.sendConfirmationCode(loginRequest.getEmail());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        if (!authService.checkConfirmationCode(loginRequest.getEmail(), loginRequest.getConfirmationCode()))
            return ResponseEntity.badRequest().build();
        AuthResult authResult = authService.signup(loginRequest);
        
        return ResponseEntity.ok()
                .headers(h -> authResult.cookies().forEach(c -> h.add(HttpHeaders.SET_COOKIE, c.toString())))
                .body(authResult.body());
    }

    @GetMapping("/test/send")
    public String sendEmail() {
        emailService.sendSimpleMessage("olivertwistyes@gmail.com", "TEST", "Проверка");
        return "Проверяйте почту";
    }

}
