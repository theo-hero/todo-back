package com.example.spring_boot.dto;
import java.util.List;

import org.springframework.http.ResponseCookie;

public record AuthResult(LoginResponse body, List<ResponseCookie> cookies) {}