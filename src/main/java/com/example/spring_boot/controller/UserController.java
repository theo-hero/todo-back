package com.example.spring_boot.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot.dto.AuthResult;
import com.example.spring_boot.dto.LoginRequest;
import com.example.spring_boot.dto.LoginResponse;
import com.example.spring_boot.model.AppUser;
import com.example.spring_boot.model.BotCode;
import com.example.spring_boot.repository.BotCodeRepository;
import com.example.spring_boot.repository.UserRepository;
import com.example.spring_boot.service.AuthService;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private BotCodeRepository botCodeRepository;

    private final SecureRandom random = new SecureRandom();

    @GetMapping("/test")
    public String sayHello() {
        return "HEY! Can see without authentication";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
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
    public ResponseEntity<?> signUp(@RequestBody LoginRequest loginRequest) {
        if (!authService.checkConfirmationCode(loginRequest.getEmail(), loginRequest.getConfirmationCode()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при проверке кода подтверждения");
        AuthResult authResult = authService.signup(loginRequest);

        return ResponseEntity.ok()
                .headers(h -> authResult.cookies().forEach(c -> h.add(HttpHeaders.SET_COOKIE, c.toString())))
                .body(authResult.body());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfileData(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        LoginResponse userResponse = new LoginResponse(user.getUsername(), user.getName(), user.getEmail(), null);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/link_telegram")
    public int generateTelegramVerificationCode(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        BotCode botCode = BotCode.builder()
                .code(random.nextInt(100_000))
                .expiresAt(Instant.now().plus(10, ChronoUnit.MINUTES))
                .user(userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new Exception("Пользователь не найден")))
                .build();
        
        botCodeRepository.save(botCode);

        return botCode.getCode();
    }
}
