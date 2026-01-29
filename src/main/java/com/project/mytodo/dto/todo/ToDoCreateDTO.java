package com.project.mytodo.dto.todo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoCreateDTO {
    private Long categorieId;
    private Long statutId;
    private String titre;
    private String description;
    private LocalDateTime dateEcheance;
}
