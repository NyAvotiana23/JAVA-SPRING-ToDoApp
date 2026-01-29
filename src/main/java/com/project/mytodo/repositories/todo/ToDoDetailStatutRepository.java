package com.project.mytodo.repositories.todo;

import com.project.mytodo.entities.todo.ToDoDetailStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoDetailStatutRepository extends JpaRepository<ToDoDetailStatut, Long> {
}
