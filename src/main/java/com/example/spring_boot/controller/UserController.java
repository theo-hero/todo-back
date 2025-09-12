package com.example.spring_boot.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot.model.User;
import com.example.spring_boot.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }
    

}
