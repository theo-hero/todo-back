package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_boot.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    
}