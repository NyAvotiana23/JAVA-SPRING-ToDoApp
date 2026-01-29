package com.project.mytodo.dto.todo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoCategorieDTO {
    private Long id;
    private Long userId;
    private String nom;
    private String color;
}
