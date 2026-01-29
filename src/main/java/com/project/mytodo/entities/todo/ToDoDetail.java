package com.project.mytodo.entities.todo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "todo_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_todo", nullable = false)
    private ToDo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_statut_detail")
    private ToDoDetailStatut statutDetail;

    @Column(nullable = false)
    private String tache;

    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    @Column
    private Integer priorite;

    @Column
    private Integer avancement;
}
