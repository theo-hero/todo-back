package com.example.spring_boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkTelegramRequest {
    private Long telegramId;
    private int code;
}
