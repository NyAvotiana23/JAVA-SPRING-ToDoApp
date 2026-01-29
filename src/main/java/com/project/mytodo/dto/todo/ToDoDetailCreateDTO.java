package com.project.mytodo.dto.todo;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDetailCreateDTO {
    private Long todoId;
    private Long statutDetailId;
    private String tache;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer priorite;
    private Integer avancement;
}
