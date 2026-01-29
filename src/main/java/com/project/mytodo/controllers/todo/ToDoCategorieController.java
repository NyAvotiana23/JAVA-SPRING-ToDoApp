package com.project.mytodo.controllers.todo;

import com.project.mytodo.dto.todo.ToDoCategorieCreateDTO;
import com.project.mytodo.dto.todo.ToDoCategorieDTO;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.services.todo.ToDoCategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class ToDoCategorieController {

    private final ToDoCategorieService categorieService;

    @GetMapping
    public ResponseEntity<List<ToDoCategorieDTO>> getAllCategories(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categorieService.getAllByUser(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoCategorieDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ToDoCategorieDTO>> searchCategories(
            @AuthenticationPrincipal User user,
            @RequestParam String nom) {
        return ResponseEntity.ok(categorieService.search(user.getId(), nom));
    }

    @PostMapping
    public ResponseEntity<ToDoCategorieDTO> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody ToDoCategorieCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categorieService.create(user.getId(), createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoCategorieDTO> updateCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestBody ToDoCategorieCreateDTO updateDTO) {
        return ResponseEntity.ok(categorieService.update(id, user.getId(), updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        categorieService.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
