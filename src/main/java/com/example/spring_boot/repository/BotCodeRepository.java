package com.example.spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.spring_boot.model.BotCode;

public interface BotCodeRepository extends JpaRepository<BotCode, Long> {
    Optional<BotCode> findByCodeAndUsedFalse(int code);
}
