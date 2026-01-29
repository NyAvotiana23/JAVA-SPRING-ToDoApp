package com.project.mytodo.controllers.todo;

import com.project.mytodo.dto.todo.ToDoCreateDTO;
import com.project.mytodo.dto.todo.ToDoDTO;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.services.todo.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService todoService;

    @GetMapping
    public ResponseEntity<List<ToDoDTO>> getAllTodos(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.getAllByUser(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoDTO> getTodoById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.getById(id, user.getId()));
    }

    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<ToDoDTO>> getTodosByCategorie(
            @PathVariable Long categorieId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.getByCategorie(user.getId(), categorieId));
    }

    @GetMapping("/statut/{statutId}")
    public ResponseEntity<List<ToDoDTO>> getTodosByStatut(
            @PathVariable Long statutId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.getByStatut(user.getId(), statutId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ToDoDTO>> searchTodos(
            @RequestParam String titre,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.search(user.getId(), titre));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ToDoDTO>> getTodosByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.getByDateRange(user.getId(), start, end));
    }

    @PostMapping
    public ResponseEntity<ToDoDTO> createTodo(
            @RequestBody ToDoCreateDTO createDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(todoService.create(user.getId(), createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoDTO> updateTodo(
            @PathVariable Long id,
            @RequestBody ToDoCreateDTO updateDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(todoService.update(id, user.getId(), updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        todoService.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
