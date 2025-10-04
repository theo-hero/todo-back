package com.example.spring_boot.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_verification_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationCode {
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private Instant expiresAt;
    private String code;

    @Builder.Default
    private int attempts = 3;
    @Builder.Default
    private boolean used = false;
}
