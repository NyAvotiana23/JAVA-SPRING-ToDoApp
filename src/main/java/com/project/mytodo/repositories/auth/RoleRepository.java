package com.project.mytodo.repositories.auth;


import com.project.mytodo.entities.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
