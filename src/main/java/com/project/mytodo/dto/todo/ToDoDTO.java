package com.project.mytodo.dto.todo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDTO {
    private Long id;
    private Long userId;
    private Long categorieId;
    private String categorieNom;
    private Long statutId;
    private String statutNom;
    private String titre;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime dateEcheance;
    private List<ToDoDetailDTO> details;
}
