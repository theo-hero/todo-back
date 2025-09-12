package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_boot.model.Task;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatedBy_Id(Long id);
}