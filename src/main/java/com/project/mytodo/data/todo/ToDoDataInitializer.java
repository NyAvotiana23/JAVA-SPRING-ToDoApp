package com.project.mytodo.data.todo;

import com.project.mytodo.entities.auth.User;
import com.project.mytodo.entities.todo.*;
import com.project.mytodo.repositories.auth.UserRepository;
import com.project.mytodo.repositories.todo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class ToDoDataInitializer {

    @Bean
    @Order(2) // Execute after auth data initializer
    public CommandLineRunner initToDoData(
            UserRepository userRepository,
            ToDoCategorieRepository categorieRepository,
            ToDoStatutRepository statutRepository,
            ToDoDetailStatutRepository detailStatutRepository,
            ToDoRepository todoRepository,
            ToDoDetailRepository detailRepository) {

        return args -> {
            // Create ToDo Statuts if they don't exist
            ToDoStatut statutAFaire = createStatutIfNotExists(statutRepository, "À faire", 0, "<span class=\"badge bg-secondary\">À faire</span>");
            ToDoStatut statutEnCours = createStatutIfNotExists(statutRepository, "En cours", 50, "<span class=\"badge bg-primary\">En cours</span>");
            ToDoStatut statutTermine = createStatutIfNotExists(statutRepository, "Terminé", 100, "<span class=\"badge bg-success\">Terminé</span>");
            ToDoStatut statutAnnule = createStatutIfNotExists(statutRepository, "Annulé", 0, "<span class=\"badge bg-danger\">Annulé</span>");

            // Create ToDoDetail Statuts if they don't exist
            ToDoDetailStatut detailStatutNonCommence = createDetailStatutIfNotExists(detailStatutRepository, "Non commencé", 0, "<span class=\"badge bg-light text-dark\">Non commencé</span>");
            ToDoDetailStatut detailStatutEnCours = createDetailStatutIfNotExists(detailStatutRepository, "En cours", 50, "<span class=\"badge bg-info\">En cours</span>");
            ToDoDetailStatut detailStatutTermine = createDetailStatutIfNotExists(detailStatutRepository, "Terminé", 100, "<span class=\"badge bg-success\">Terminé</span>");
            ToDoDetailStatut detailStatutBloque = createDetailStatutIfNotExists(detailStatutRepository, "Bloqué", 25, "<span class=\"badge bg-warning\">Bloqué</span>");

            // Get sample user
            User user = userRepository.findByUsername("user");
            if (user != null) {
                // Create Categories for user if they don't exist
                ToDoCategorie categorieTravail = createCategorieIfNotExists(categorieRepository, user, "Travail", "#3498db");
                ToDoCategorie categoriePersonnel = createCategorieIfNotExists(categorieRepository, user, "Personnel", "#2ecc71");
                ToDoCategorie categorieCourses = createCategorieIfNotExists(categorieRepository, user, "Courses", "#e74c3c");
                ToDoCategorie categorieSante = createCategorieIfNotExists(categorieRepository, user, "Santé", "#9b59b6");

                // Create sample ToDos if empty
                if (todoRepository.findByUserId(user.getId()).isEmpty()) {
                    // ToDo 1: Projet de développement
                    ToDo todo1 = ToDo.builder()
                            .user(user)
                            .categorie(categorieTravail)
                            .statut(statutEnCours)
                            .titre("Finaliser le projet Spring Boot")
                            .description("Compléter le développement de l'API REST pour l'application todo")
                            .dateEcheance(LocalDateTime.now().plusDays(7))
                            .build();
                    todo1 = todoRepository.save(todo1);

                    // Details for ToDo 1
                    createDetail(detailRepository, todo1, detailStatutTermine, "Créer les entités JPA", LocalTime.of(9, 0), LocalTime.of(11, 0), 1, 100);
                    createDetail(detailRepository, todo1, detailStatutTermine, "Implémenter les repositories", LocalTime.of(11, 0), LocalTime.of(12, 0), 2, 100);
                    createDetail(detailRepository, todo1, detailStatutEnCours, "Créer les services", LocalTime.of(14, 0), LocalTime.of(16, 0), 3, 75);
                    createDetail(detailRepository, todo1, detailStatutNonCommence, "Écrire les tests unitaires", LocalTime.of(16, 0), LocalTime.of(18, 0), 4, 0);

                    // ToDo 2: Courses
                    ToDo todo2 = ToDo.builder()
                            .user(user)
                            .categorie(categorieCourses)
                            .statut(statutAFaire)
                            .titre("Faire les courses de la semaine")
                            .description("Liste des courses pour la semaine")
                            .dateEcheance(LocalDateTime.now().plusDays(2))
                            .build();
                    todo2 = todoRepository.save(todo2);

                    // Details for ToDo 2
                    createDetail(detailRepository, todo2, detailStatutNonCommence, "Fruits et légumes", null, null, 1, 0);
                    createDetail(detailRepository, todo2, detailStatutNonCommence, "Produits laitiers", null, null, 2, 0);
                    createDetail(detailRepository, todo2, detailStatutNonCommence, "Viande et poisson", null, null, 3, 0);

                    // ToDo 3: Sport
                    ToDo todo3 = ToDo.builder()
                            .user(user)
                            .categorie(categorieSante)
                            .statut(statutEnCours)
                            .titre("Programme de remise en forme")
                            .description("Suivre le programme d'exercices hebdomadaire")
                            .dateEcheance(LocalDateTime.now().plusDays(30))
                            .build();
                    todo3 = todoRepository.save(todo3);

                    // Details for ToDo 3
                    createDetail(detailRepository, todo3, detailStatutTermine, "Lundi - Cardio 30min", LocalTime.of(7, 0), LocalTime.of(7, 30), 1, 100);
                    createDetail(detailRepository, todo3, detailStatutEnCours, "Mercredi - Musculation", LocalTime.of(18, 0), LocalTime.of(19, 0), 2, 50);
                    createDetail(detailRepository, todo3, detailStatutNonCommence, "Vendredi - Natation", LocalTime.of(12, 0), LocalTime.of(13, 0), 3, 0);

                    // ToDo 4: Personnel
                    ToDo todo4 = ToDo.builder()
                            .user(user)
                            .categorie(categoriePersonnel)
                            .statut(statutTermine)
                            .titre("Organiser l'anniversaire")
                            .description("Préparer la fête d'anniversaire surprise")
                            .dateEcheance(LocalDateTime.now().minusDays(3))
                            .build();
                    todo4 = todoRepository.save(todo4);

                    // Details for ToDo 4
                    createDetail(detailRepository, todo4, detailStatutTermine, "Réserver le lieu", null, null, 1, 100);
                    createDetail(detailRepository, todo4, detailStatutTermine, "Commander le gâteau", null, null, 2, 100);
                    createDetail(detailRepository, todo4, detailStatutTermine, "Envoyer les invitations", null, null, 3, 100);
                }
            }

            // Get admin user and create some data for them too
            User admin = userRepository.findByUsername("admin");
            if (admin != null) {
                createCategorieIfNotExists(categorieRepository, admin, "Administration", "#e67e22");
                createCategorieIfNotExists(categorieRepository, admin, "Projets", "#1abc9c");
            }
        };
    }

    private ToDoStatut createStatutIfNotExists(ToDoStatutRepository repository, String statut, Integer score, String spanHtml) {
        return repository.findAll().stream()
                .filter(s -> s.getStatut().equals(statut))
                .findFirst()
                .orElseGet(() -> repository.save(ToDoStatut.builder()
                        .statut(statut)
                        .score(score)
                        .spanHtml(spanHtml)
                        .build()));
    }

    private ToDoDetailStatut createDetailStatutIfNotExists(ToDoDetailStatutRepository repository, String statut, Integer score, String spanHtml) {
        return repository.findAll().stream()
                .filter(s -> s.getStatut().equals(statut))
                .findFirst()
                .orElseGet(() -> repository.save(ToDoDetailStatut.builder()
                        .statut(statut)
                        .score(score)
                        .spanHtml(spanHtml)
                        .build()));
    }

    private ToDoCategorie createCategorieIfNotExists(ToDoCategorieRepository repository, User user, String nom, String color) {
        return repository.findByUserId(user.getId()).stream()
                .filter(c -> c.getNom().equals(nom))
                .findFirst()
                .orElseGet(() -> repository.save(ToDoCategorie.builder()
                        .user(user)
                        .nom(nom)
                        .color(color)
                        .build()));
    }

    private void createDetail(ToDoDetailRepository repository, ToDo todo, ToDoDetailStatut statut,
                              String tache, LocalTime heureDebut, LocalTime heureFin, Integer priorite, Integer avancement) {
        repository.save(ToDoDetail.builder()
                .todo(todo)
                .statutDetail(statut)
                .tache(tache)
                .heureDebut(heureDebut)
                .heureFin(heureFin)
                .priorite(priorite)
                .avancement(avancement)
                .build());
    }
}
