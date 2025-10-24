package com.example.spring_boot.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.spring_boot.model.Task;

@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Test
    public void saveTask() {
        Task task = Task.builder()
                .description("помыть посуду")
                .createdBy(userRepository.findById(202L).orElse(null)).build();

        Task saved = taskRepository.save(task);
    }
}
