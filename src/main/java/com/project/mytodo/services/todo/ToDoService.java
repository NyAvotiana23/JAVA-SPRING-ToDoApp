package com.project.mytodo.services.todo;

import com.project.mytodo.dto.todo.ToDoCreateDTO;
import com.project.mytodo.dto.todo.ToDoDTO;
import com.project.mytodo.dto.todo.ToDoDetailDTO;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.entities.todo.ToDo;
import com.project.mytodo.entities.todo.ToDoCategorie;
import com.project.mytodo.entities.todo.ToDoStatut;
import com.project.mytodo.repositories.auth.UserRepository;
import com.project.mytodo.repositories.todo.ToDoCategorieRepository;
import com.project.mytodo.repositories.todo.ToDoRepository;
import com.project.mytodo.repositories.todo.ToDoStatutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ToDoService {

    private final ToDoRepository todoRepository;
    private final UserRepository userRepository;
    private final ToDoCategorieRepository categorieRepository;
    private final ToDoStatutRepository statutRepository;

    public List<ToDoDTO> getAllByUser(Long userId) {
        return todoRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ToDoDTO getById(Long id, Long userId) {
        return todoRepository.findByIdAndUserId(id, userId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("ToDo non trouvé"));
    }

    public ToDoDTO create(Long userId, ToDoCreateDTO createDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ToDo todo = ToDo.builder()
                .user(user)
                .titre(createDTO.getTitre())
                .description(createDTO.getDescription())
                .dateEcheance(createDTO.getDateEcheance())
                .build();

        if (createDTO.getCategorieId() != null) {
            ToDoCategorie categorie = categorieRepository.findById(createDTO.getCategorieId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            todo.setCategorie(categorie);
        }

        if (createDTO.getStatutId() != null) {
            ToDoStatut statut = statutRepository.findById(createDTO.getStatutId())
                    .orElseThrow(() -> new RuntimeException("Statut non trouvé"));
            todo.setStatut(statut);
        }

        return toDTO(todoRepository.save(todo));
    }

    public ToDoDTO update(Long id, Long userId, ToDoCreateDTO updateDTO) {
        ToDo todo = todoRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("ToDo non trouvé"));

        todo.setTitre(updateDTO.getTitre());
        todo.setDescription(updateDTO.getDescription());
        todo.setDateEcheance(updateDTO.getDateEcheance());

        if (updateDTO.getCategorieId() != null) {
            ToDoCategorie categorie = categorieRepository.findById(updateDTO.getCategorieId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            todo.setCategorie(categorie);
        } else {
            todo.setCategorie(null);
        }

        if (updateDTO.getStatutId() != null) {
            ToDoStatut statut = statutRepository.findById(updateDTO.getStatutId())
                    .orElseThrow(() -> new RuntimeException("Statut non trouvé"));
            todo.setStatut(statut);
        } else {
            todo.setStatut(null);
        }

        return toDTO(todoRepository.save(todo));
    }

    public void delete(Long id, Long userId) {
        ToDo todo = todoRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("ToDo non trouvé"));
        todoRepository.delete(todo);
    }

    public List<ToDoDTO> getByCategorie(Long userId, Long categorieId) {
        return todoRepository.findByUserIdAndCategorieId(userId, categorieId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ToDoDTO> getByStatut(Long userId, Long statutId) {
        return todoRepository.findByUserIdAndStatutId(userId, statutId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ToDoDTO> search(Long userId, String titre) {
        return todoRepository.searchByTitre(userId, titre)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ToDoDTO> getByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return todoRepository.findByUserIdAndDateEcheanceBetween(userId, start, end)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ToDoDTO toDTO(ToDo todo) {
        List<ToDoDetailDTO> detailDTOs = todo.getDetails() != null ?
                todo.getDetails().stream()
                        .map(detail -> ToDoDetailDTO.builder()
                                .id(detail.getId())
                                .todoId(todo.getId())
                                .statutDetailId(detail.getStatutDetail() != null ? detail.getStatutDetail().getId() : null)
                                .statutDetailNom(detail.getStatutDetail() != null ? detail.getStatutDetail().getStatut() : null)
                                .tache(detail.getTache())
                                .heureDebut(detail.getHeureDebut())
                                .heureFin(detail.getHeureFin())
                                .priorite(detail.getPriorite())
                                .avancement(detail.getAvancement())
                                .build())
                        .collect(Collectors.toList()) : Collections.emptyList();

        return ToDoDTO.builder()
                .id(todo.getId())
                .userId(todo.getUser().getId())
                .categorieId(todo.getCategorie() != null ? todo.getCategorie().getId() : null)
                .categorieNom(todo.getCategorie() != null ? todo.getCategorie().getNom() : null)
                .statutId(todo.getStatut() != null ? todo.getStatut().getId() : null)
                .statutNom(todo.getStatut() != null ? todo.getStatut().getStatut() : null)
                .titre(todo.getTitre())
                .description(todo.getDescription())
                .dateCreation(todo.getDateCreation())
                .dateEcheance(todo.getDateEcheance())
                .details(detailDTOs)
                .build();
    }
}
