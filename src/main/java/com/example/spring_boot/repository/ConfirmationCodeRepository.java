package com.example.spring_boot.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_boot.model.ConfirmationCode;


@RepositoryRestResource
public interface ConfirmationCodeRepository  extends JpaRepository<ConfirmationCode, Long> {
    ConfirmationCode findFirstByEmailAndUsedFalseOrderByIdDesc(String email);
}
