package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.spring_boot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
