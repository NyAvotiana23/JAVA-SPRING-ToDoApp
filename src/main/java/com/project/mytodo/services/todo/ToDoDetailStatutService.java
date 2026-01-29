package com.project.mytodo.services.todo;

import com.project.mytodo.dto.todo.ToDoDetailStatutDTO;
import com.project.mytodo.entities.todo.ToDoDetailStatut;
import com.project.mytodo.repositories.todo.ToDoDetailStatutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ToDoDetailStatutService {

    private final ToDoDetailStatutRepository detailStatutRepository;

    public List<ToDoDetailStatutDTO> getAll() {
        return detailStatutRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ToDoDetailStatutDTO getById(Long id) {
        return detailStatutRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Statut de détail non trouvé"));
    }

    public ToDoDetailStatutDTO create(ToDoDetailStatutDTO dto) {
        ToDoDetailStatut statut = ToDoDetailStatut.builder()
                .statut(dto.getStatut())
                .score(dto.getScore())
                .spanHtml(dto.getSpanHtml())
                .build();

        return toDTO(detailStatutRepository.save(statut));
    }

    public ToDoDetailStatutDTO update(Long id, ToDoDetailStatutDTO dto) {
        ToDoDetailStatut statut = detailStatutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statut de détail non trouvé"));

        statut.setStatut(dto.getStatut());
        statut.setScore(dto.getScore());
        statut.setSpanHtml(dto.getSpanHtml());

        return toDTO(detailStatutRepository.save(statut));
    }

    public void delete(Long id) {
        if (!detailStatutRepository.existsById(id)) {
            throw new RuntimeException("Statut de détail non trouvé");
        }
        detailStatutRepository.deleteById(id);
    }

    private ToDoDetailStatutDTO toDTO(ToDoDetailStatut statut) {
        return ToDoDetailStatutDTO.builder()
                .id(statut.getId())
                .statut(statut.getStatut())
                .score(statut.getScore())
                .spanHtml(statut.getSpanHtml())
                .build();
    }
}
