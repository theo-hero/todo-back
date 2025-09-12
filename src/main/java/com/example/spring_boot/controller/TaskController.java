package com.example.spring_boot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot.model.Task;
import com.example.spring_boot.repository.TaskRepository;
import com.example.spring_boot.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasksByUser(@RequestParam("user-id") Long userId) {
        if (userRepository.existsById(userId)) {
            return ResponseEntity.ok(taskRepository.findByCreatedBy_Id(userId));
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "User " + userId + " not found"));
    }

    @PostMapping("/tasks")
    public void postTask(@RequestBody Task task) {
        taskRepository.save(task);
    }

    @DeleteMapping("/tasks")
    public void deleteTask(@RequestParam("id") Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @PutMapping("/tasks")
    public void putMethodName(@RequestParam("id") Long taskId, @RequestBody Task entity) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No such taskId"));
        task.setDeadline(entity.getDeadline());
        task.setDescription(entity.getDescription());
        task.setEstimatedTime(entity.getEstimatedTime());
        task.setStatus(entity.getStatus());
        task.setTeamId(entity.getTeamId());
        taskRepository.save(task);
    }
}
