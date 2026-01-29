package com.project.mytodo.services.todo;

import com.project.mytodo.dto.todo.ToDoStatutDTO;
import com.project.mytodo.entities.todo.ToDoStatut;
import com.project.mytodo.repositories.todo.ToDoStatutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ToDoStatutService {

    private final ToDoStatutRepository statutRepository;

    public List<ToDoStatutDTO> getAll() {
        return statutRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ToDoStatutDTO getById(Long id) {
        return statutRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Statut non trouvé"));
    }

    public ToDoStatutDTO create(ToDoStatutDTO dto) {
        ToDoStatut statut = ToDoStatut.builder()
                .statut(dto.getStatut())
                .score(dto.getScore())
                .spanHtml(dto.getSpanHtml())
                .build();

        return toDTO(statutRepository.save(statut));
    }

    public ToDoStatutDTO update(Long id, ToDoStatutDTO dto) {
        ToDoStatut statut = statutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statut non trouvé"));

        statut.setStatut(dto.getStatut());
        statut.setScore(dto.getScore());
        statut.setSpanHtml(dto.getSpanHtml());

        return toDTO(statutRepository.save(statut));
    }

    public void delete(Long id) {
        if (!statutRepository.existsById(id)) {
            throw new RuntimeException("Statut non trouvé");
        }
        statutRepository.deleteById(id);
    }

    private ToDoStatutDTO toDTO(ToDoStatut statut) {
        return ToDoStatutDTO.builder()
                .id(statut.getId())
                .statut(statut.getStatut())
                .score(statut.getScore())
                .spanHtml(statut.getSpanHtml())
                .build();
    }
}
