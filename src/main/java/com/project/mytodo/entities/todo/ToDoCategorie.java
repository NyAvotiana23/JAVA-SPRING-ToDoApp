package com.project.mytodo.entities.todo;

import com.project.mytodo.entities.auth.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "todo_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoCategorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false)
    private String nom;

    @Column
    private String color;
}
