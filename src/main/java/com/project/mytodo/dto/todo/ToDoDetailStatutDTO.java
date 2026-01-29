package com.project.mytodo.dto.todo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDetailStatutDTO {
    private Long id;
    private String statut;
    private Integer score;
    private String spanHtml;
}
