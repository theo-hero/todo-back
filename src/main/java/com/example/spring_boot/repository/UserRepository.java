package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_boot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
