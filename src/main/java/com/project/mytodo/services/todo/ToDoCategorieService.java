package com.project.mytodo.services.todo;

import com.project.mytodo.dto.todo.ToDoCategorieCreateDTO;
import com.project.mytodo.dto.todo.ToDoCategorieDTO;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.entities.todo.ToDoCategorie;
import com.project.mytodo.repositories.auth.UserRepository;
import com.project.mytodo.repositories.todo.ToDoCategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ToDoCategorieService {

    private final ToDoCategorieRepository categorieRepository;
    private final UserRepository userRepository;

    public List<ToDoCategorieDTO> getAllByUser(Long userId) {
        return categorieRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ToDoCategorieDTO getById(Long id) {
        return categorieRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
    }

    public ToDoCategorieDTO create(Long userId, ToDoCategorieCreateDTO createDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ToDoCategorie categorie = ToDoCategorie.builder()
                .user(user)
                .nom(createDTO.getNom())
                .color(createDTO.getColor())
                .build();

        return toDTO(categorieRepository.save(categorie));
    }

    public ToDoCategorieDTO update(Long id, Long userId, ToDoCategorieCreateDTO updateDTO) {
        ToDoCategorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        if (!categorie.getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès non autorisé à cette catégorie");
        }

        categorie.setNom(updateDTO.getNom());
        categorie.setColor(updateDTO.getColor());

        return toDTO(categorieRepository.save(categorie));
    }

    public void delete(Long id, Long userId) {
        ToDoCategorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        if (!categorie.getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès non autorisé à cette catégorie");
        }

        categorieRepository.delete(categorie);
    }

    public List<ToDoCategorieDTO> search(Long userId, String nom) {
        return categorieRepository.findByUserIdAndNomContainingIgnoreCase(userId, nom)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ToDoCategorieDTO toDTO(ToDoCategorie categorie) {
        return ToDoCategorieDTO.builder()
                .id(categorie.getId())
                .userId(categorie.getUser().getId())
                .nom(categorie.getNom())
                .color(categorie.getColor())
                .build();
    }
}
