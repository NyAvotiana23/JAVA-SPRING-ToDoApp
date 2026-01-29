package com.project.mytodo.controllers.todo;

import com.project.mytodo.dto.todo.ToDoDetailStatutDTO;
import com.project.mytodo.services.todo.ToDoDetailStatutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detail-statuts")
@RequiredArgsConstructor
public class ToDoDetailStatutController {

    private final ToDoDetailStatutService detailStatutService;

    @GetMapping
    public ResponseEntity<List<ToDoDetailStatutDTO>> getAllDetailStatuts() {
        return ResponseEntity.ok(detailStatutService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoDetailStatutDTO> getDetailStatutById(@PathVariable Long id) {
        return ResponseEntity.ok(detailStatutService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ToDoDetailStatutDTO> createDetailStatut(@RequestBody ToDoDetailStatutDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detailStatutService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoDetailStatutDTO> updateDetailStatut(
            @PathVariable Long id,
            @RequestBody ToDoDetailStatutDTO dto) {
        return ResponseEntity.ok(detailStatutService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetailStatut(@PathVariable Long id) {
        detailStatutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
