package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_boot.model.TelegramLink;

public interface TelegramLinkRepository extends JpaRepository<TelegramLink, Long> {
    
}