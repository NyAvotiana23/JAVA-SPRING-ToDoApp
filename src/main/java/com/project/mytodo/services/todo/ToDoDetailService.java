package com.project.mytodo.services.todo;

import com.project.mytodo.dto.todo.ToDoDetailCreateDTO;
import com.project.mytodo.dto.todo.ToDoDetailDTO;
import com.project.mytodo.entities.todo.ToDo;
import com.project.mytodo.entities.todo.ToDoDetail;
import com.project.mytodo.entities.todo.ToDoDetailStatut;
import com.project.mytodo.repositories.todo.ToDoDetailRepository;
import com.project.mytodo.repositories.todo.ToDoDetailStatutRepository;
import com.project.mytodo.repositories.todo.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ToDoDetailService {

    private final ToDoDetailRepository detailRepository;
    private final ToDoRepository todoRepository;
    private final ToDoDetailStatutRepository detailStatutRepository;

    public List<ToDoDetailDTO> getAllByTodoId(Long todoId) {
        return detailRepository.findByTodoIdOrderByPrioriteAsc(todoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ToDoDetailDTO getById(Long id) {
        return detailRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Détail non trouvé"));
    }

    public ToDoDetailDTO create(Long userId, ToDoDetailCreateDTO createDTO) {
        ToDo todo = todoRepository.findByIdAndUserId(createDTO.getTodoId(), userId)
                .orElseThrow(() -> new RuntimeException("ToDo non trouvé ou accès non autorisé"));

        ToDoDetail detail = ToDoDetail.builder()
                .todo(todo)
                .tache(createDTO.getTache())
                .heureDebut(createDTO.getHeureDebut())
                .heureFin(createDTO.getHeureFin())
                .priorite(createDTO.getPriorite())
                .avancement(createDTO.getAvancement())
                .build();

        if (createDTO.getStatutDetailId() != null) {
            ToDoDetailStatut statutDetail = detailStatutRepository.findById(createDTO.getStatutDetailId())
                    .orElseThrow(() -> new RuntimeException("Statut de détail non trouvé"));
            detail.setStatutDetail(statutDetail);
        }

        return toDTO(detailRepository.save(detail));
    }

    public ToDoDetailDTO update(Long id, Long userId, ToDoDetailCreateDTO updateDTO) {
        ToDoDetail detail = detailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Détail non trouvé"));

        // Vérifier que l'utilisateur est propriétaire du ToDo parent
        if (!detail.getTodo().getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès non autorisé à ce détail");
        }

        detail.setTache(updateDTO.getTache());
        detail.setHeureDebut(updateDTO.getHeureDebut());
        detail.setHeureFin(updateDTO.getHeureFin());
        detail.setPriorite(updateDTO.getPriorite());
        detail.setAvancement(updateDTO.getAvancement());

        if (updateDTO.getStatutDetailId() != null) {
            ToDoDetailStatut statutDetail = detailStatutRepository.findById(updateDTO.getStatutDetailId())
                    .orElseThrow(() -> new RuntimeException("Statut de détail non trouvé"));
            detail.setStatutDetail(statutDetail);
        } else {
            detail.setStatutDetail(null);
        }

        return toDTO(detailRepository.save(detail));
    }

    public void delete(Long id, Long userId) {
        ToDoDetail detail = detailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Détail non trouvé"));

        // Vérifier que l'utilisateur est propriétaire du ToDo parent
        if (!detail.getTodo().getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès non autorisé à ce détail");
        }

        detailRepository.delete(detail);
    }

    private ToDoDetailDTO toDTO(ToDoDetail detail) {
        return ToDoDetailDTO.builder()
                .id(detail.getId())
                .todoId(detail.getTodo().getId())
                .statutDetailId(detail.getStatutDetail() != null ? detail.getStatutDetail().getId() : null)
                .statutDetailNom(detail.getStatutDetail() != null ? detail.getStatutDetail().getStatut() : null)
                .tache(detail.getTache())
                .heureDebut(detail.getHeureDebut())
                .heureFin(detail.getHeureFin())
                .priorite(detail.getPriorite())
                .avancement(detail.getAvancement())
                .build();
    }
}
