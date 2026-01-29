package com.project.mytodo.entities.todo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "todo_detail_statuts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDetailStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String statut;

    @Column
    private Integer score;

    @Column(name = "span_html")
    private String spanHtml;
}
