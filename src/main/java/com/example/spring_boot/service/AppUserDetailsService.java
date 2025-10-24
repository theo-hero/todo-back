package com.example.spring_boot.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_boot.model.AppUser;
import com.example.spring_boot.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        AppUser user = (username.contains("@"))
        ? userRepository.findByEmail(username.trim()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"))
        : userRepository.findByUsername(username.trim()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        return (User) User.withUsername(username)
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .disabled(false)
                .build();
    }
}
