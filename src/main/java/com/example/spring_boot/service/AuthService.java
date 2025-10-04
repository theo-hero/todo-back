package com.example.spring_boot.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.spring_boot.dto.AuthResult;
import com.example.spring_boot.dto.LoginRequest;
import com.example.spring_boot.dto.LoginResponse;
import com.example.spring_boot.jwt.JwtUtils;
import com.example.spring_boot.model.ConfirmationCode;
import com.example.spring_boot.repository.ConfirmationCodeRepository;
import com.example.spring_boot.repository.UserRepository;
@Service
public class AuthService {

        private final UserRepository userRepository;
        private final JwtUtils jwtUtils;
        private final AuthenticationManager authenticationManager;
        private final UserDetailsManager userDetailsManager;
        private final PasswordEncoder passwordEncoder;
        private final EmailService emailService;
        private final ConfirmationCodeRepository confirmationCodeRepository;

        private final SecureRandom random = new SecureRandom();

        private final int maxAgeSeconds;
        private final int confirmationCodeAge;

        public AuthService(
                        UserRepository userRepository,
                        JwtUtils jwtUtils,
                        AuthenticationManager authenticationManager,
                        UserDetailsManager userDetailsManager,
                        PasswordEncoder passwordEncoder,
                        EmailService emailService,
                        ConfirmationCodeRepository confirmationCodeRepository,
                        @Value("${spring.app.jwtExpirationMs}") long jwtExpirationMs,
                        @Value("${app.confirmation-code.ttl-seconds:360}") int confirmationCodeAgeSeconds) {
                this.userRepository = userRepository;
                this.jwtUtils = jwtUtils;
                this.authenticationManager = authenticationManager;
                this.userDetailsManager = userDetailsManager;
                this.passwordEncoder = passwordEncoder;
                this.emailService = emailService;
                this.confirmationCodeRepository = confirmationCodeRepository;

                this.maxAgeSeconds = (int) (jwtExpirationMs / 1000);
                this.confirmationCodeAge = confirmationCodeAgeSeconds;
        }

        public AuthResult signin(LoginRequest request) {
                Authentication auth = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                UserDetails principal = (UserDetails) auth.getPrincipal();

                String token = jwtUtils.generateTokenFromUsername(principal);

                List<String> roles = principal.getAuthorities().stream()
                                .map(a -> a.getAuthority())
                                .toList();

                LoginResponse body = new LoginResponse(principal.getUsername(), roles);

                ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(maxAgeSeconds)
                                .sameSite("Lax")
                                .build();

                ResponseCookie usernameCookie = ResponseCookie.from("username", request.getUsername())
                                .path("/")
                                .maxAge(maxAgeSeconds)
                                .sameSite("Lax")
                                .build();

                return new AuthResult(
                                body,
                                List.of(usernameCookie, jwtCookie));
        }

        public AuthResult signup(LoginRequest request) {
                String username = request.getUsername().trim();
                if (userDetailsManager.userExists(username)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this username already exists.");
                }
                UserDetails newUser = User.withUsername(username)
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles("USER")
                                .build();
                userDetailsManager.createUser(newUser);
                return signin(request);
        }

        public void sendConfirmationCode(String email) {
                Instant time = Instant.now();

                String code = String.valueOf(random.nextInt(10_000));

                ConfirmationCode codeObj = ConfirmationCode.builder()
                                .email(email)
                                .code(code)
                                .expiresAt(time.plus(confirmationCodeAge, ChronoUnit.MINUTES))
                                .build();

                confirmationCodeRepository.save(codeObj);

                emailService.sendSimpleMessage(email, "Подтверждение регистрации", "Ваш код: " + code);
        }

        public boolean checkConfirmationCode(String email, String sentCode) {
                ConfirmationCode codeObj = confirmationCodeRepository.findFirstByEmailAndUsedFalseOrderByIdDesc(email);

                if (codeObj == null)
                        return false;

                if (codeObj.getExpiresAt().isBefore(Instant.now()))
                        return false;
                if (codeObj.getAttempts() == 0)
                        return false;

                boolean codesMatch = codeObj.getCode().equals(sentCode);
                if (!codesMatch) {
                        codeObj.setAttempts(codeObj.getAttempts() - 1);
                }
                codeObj.setUsed(true);
                return codesMatch;
        }
}
