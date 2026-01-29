package com.project.mytodo.dto.todo;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDetailDTO {
    private Long id;
    private Long todoId;
    private Long statutDetailId;
    private String statutDetailNom;
    private String tache;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer priorite;
    private Integer avancement;
}
