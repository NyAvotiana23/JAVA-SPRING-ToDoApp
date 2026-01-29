package com.project.mytodo.controllers.todo;

import com.project.mytodo.dto.todo.ToDoDetailCreateDTO;
import com.project.mytodo.dto.todo.ToDoDetailDTO;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.services.todo.ToDoDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo-details")
@RequiredArgsConstructor
public class ToDoDetailController {

    private final ToDoDetailService detailService;

    @GetMapping("/todo/{todoId}")
    public ResponseEntity<List<ToDoDetailDTO>> getDetailsByTodoId(@PathVariable Long todoId) {
        return ResponseEntity.ok(detailService.getAllByTodoId(todoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoDetailDTO> getDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(detailService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ToDoDetailDTO> createDetail(
            @RequestBody ToDoDetailCreateDTO createDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detailService.create(user.getId(), createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoDetailDTO> updateDetail(
            @PathVariable Long id,
            @RequestBody ToDoDetailCreateDTO updateDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(detailService.update(id, user.getId(), updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        detailService.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
