package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.spring_boot.model.Task;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatedBy_Id(Long id);
    
    @Query(value = "select description from tasks t where t.created_by = :id", nativeQuery = true)
    List<String> findDescriptionsByUserId(Long id);
}