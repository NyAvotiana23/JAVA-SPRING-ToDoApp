package com.project.mytodo.data.auth;


import com.project.mytodo.entities.auth.Role;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.repositories.auth.RoleRepository;
import com.project.mytodo.repositories.auth.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    @Order(1) // Execute first before ToDo data initializer
    public CommandLineRunner initData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            Role userRole = roleRepository.findByName("MANAGER");
            if (userRole == null) {
                userRole = new Role("MANAGER");
                roleRepository.save(userRole);
            }

            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ADMIN");
                roleRepository.save(adminRole);
            }

            // Create sample users if they don't exist
            if (userRepository.findByUsername("user") == null) {
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);
                User user = new User("user", passwordEncoder.encode("userpass123"), userRoles);
                userRepository.save(user);
            }

            if (userRepository.findByUsername("admin") == null) {
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                User admin = new User("admin", passwordEncoder.encode("userpass123"), adminRoles);
                userRepository.save(admin);
            }
        };
    }
}