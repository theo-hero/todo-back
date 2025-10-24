package com.example.spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_boot.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<AppUser> findByEmail(String email);
}
