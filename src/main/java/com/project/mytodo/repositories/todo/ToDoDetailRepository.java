package com.project.mytodo.repositories.todo;

import com.project.mytodo.entities.todo.ToDoDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoDetailRepository extends JpaRepository<ToDoDetail, Long> {

    List<ToDoDetail> findByTodoId(Long todoId);

    List<ToDoDetail> findByTodoIdOrderByPrioriteAsc(Long todoId);

    void deleteByTodoId(Long todoId);
}
