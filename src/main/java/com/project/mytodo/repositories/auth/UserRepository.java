package com.project.mytodo.repositories.auth;

import com.project.mytodo.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}