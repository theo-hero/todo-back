package com.example.spring_boot.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.spring_boot.model.User;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void saveUser() {
        User user = User.builder().name("Оля Маркова").nickname("theomark").email("olivertwistyes@gmail.com").build();
        User saved = userRepository.save(user);

        assertTrue(saved.getName().equals("Оля Маркова"));
        assertTrue(saved.getId() != null);
    }

    @Test
    @Order(2)
    @Disabled
    public void removeUser() {
        // to be written
    }
}
