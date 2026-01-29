package com.project.mytodo.repositories.todo;

import com.project.mytodo.entities.todo.ToDoStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoStatutRepository extends JpaRepository<ToDoStatut, Long> {
}
