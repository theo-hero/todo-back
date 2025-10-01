package com.example.spring_boot.dto;

import java.time.Instant;

public record TaskResponse(
    Long id,
    String description,
    String status,
    Instant createdAt,
    String estimatedTime,
    Instant deadline,
    UserSummary createdBy,
    TeamSummary team
) {}
