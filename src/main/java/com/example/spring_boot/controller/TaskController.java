package com.example.spring_boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot.model.Task;
import com.example.spring_boot.model.User;
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

    private User getCurrentUser(Authentication auth) {
        UserDetails userData = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(userData.getUsername());
        return user;
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasksByUser(Authentication auth) {
        return ResponseEntity.ok(taskRepository.findByCreatedBy_Id(getCurrentUser(auth).getId()));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> postTask(@RequestBody Task task, Authentication auth) {
        task.setCreatedBy(getCurrentUser(auth));
        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
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
        task.setTeam(entity.getTeam());
        taskRepository.save(task);
    }
}
