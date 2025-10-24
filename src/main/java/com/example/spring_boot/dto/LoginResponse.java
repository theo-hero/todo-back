package com.example.spring_boot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String name;
    private String email;
    private List<String> roles;
}
