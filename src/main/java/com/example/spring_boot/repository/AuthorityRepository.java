package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_boot.model.Authority;
import java.util.List;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findAllByUsername(String username);
}
