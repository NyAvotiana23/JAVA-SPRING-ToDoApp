package com.project.mytodo.repositories.todo;

import com.project.mytodo.entities.todo.ToDoCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoCategorieRepository extends JpaRepository<ToDoCategorie, Long> {
    List<ToDoCategorie> findByUserId(Long userId);
    List<ToDoCategorie> findByUserIdAndNomContainingIgnoreCase(Long userId, String nom);
}
