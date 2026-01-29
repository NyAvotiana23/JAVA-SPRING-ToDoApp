package com.project.mytodo.controllers.todo;

import com.project.mytodo.dto.todo.ToDoStatutDTO;
import com.project.mytodo.services.todo.ToDoStatutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuts")
@RequiredArgsConstructor
public class ToDoStatutController {

    private final ToDoStatutService statutService;

    @GetMapping
    public ResponseEntity<List<ToDoStatutDTO>> getAllStatuts() {
        return ResponseEntity.ok(statutService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoStatutDTO> getStatutById(@PathVariable Long id) {
        return ResponseEntity.ok(statutService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ToDoStatutDTO> createStatut(@RequestBody ToDoStatutDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statutService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoStatutDTO> updateStatut(
            @PathVariable Long id,
            @RequestBody ToDoStatutDTO dto) {
        return ResponseEntity.ok(statutService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatut(@PathVariable Long id) {
        statutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
