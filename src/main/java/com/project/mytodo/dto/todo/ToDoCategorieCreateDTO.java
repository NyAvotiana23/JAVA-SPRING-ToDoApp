package com.project.mytodo.dto.todo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoCategorieCreateDTO {
    private String nom;
    private String color;
}
