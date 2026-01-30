-- =============================================
-- Script PostgreSQL - Données d'exemple MyTodo
-- =============================================

-- Nettoyage des tables (dans l'ordre des dépendances)
DELETE FROM todo_details;
DELETE FROM todos;
DELETE FROM todo_categories;
DELETE FROM todo_detail_statuts;
DELETE FROM todo_statuts;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;

-- Réinitialisation des séquences
ALTER SEQUENCE roles_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE todo_statuts_id_seq RESTART WITH 1;
ALTER SEQUENCE todo_detail_statuts_id_seq RESTART WITH 1;
ALTER SEQUENCE todo_categories_id_seq RESTART WITH 1;
ALTER SEQUENCE todos_id_seq RESTART WITH 1;
ALTER SEQUENCE todo_details_id_seq RESTART WITH 1;

-- =============================================
-- ROLES


    
-- =============================================
INSERT INTO roles (id, name) VALUES
(1, 'ADMIN'),
(2, 'MANAGER'),
(3, 'USER');

-- =============================================
-- USERS
-- Mot de passe encodé BCrypt pour: "password123"
-- =============================================
INSERT INTO users (id, username, password) VALUES
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.rsJ9HVlhIbOxPMC9.G'),
(2, 'john_doe', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.rsJ9HVlhIbOxPMC9.G'),
(3, 'jane_smith', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.rsJ9HVlhIbOxPMC9.G'),
(4, 'bob_wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.rsJ9HVlhIbOxPMC9.G');

-- =============================================
-- USER_ROLES (Association utilisateurs-rôles)
-- =============================================
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin -> ADMIN
(1, 2), -- admin -> MANAGER
(2, 2), -- john_doe -> MANAGER
(3, 2), -- jane_smith -> MANAGER
(4, 3); -- bob_wilson -> USER

-- =============================================
-- TODO_STATUTS (Statuts des tâches principales)
-- =============================================
INSERT INTO todo_statuts (id, statut, score, span_html) VALUES
(1, 'À faire', 0, '<span class="badge bg-secondary">À faire</span>'),
(2, 'En cours', 25, '<span class="badge bg-primary">En cours</span>'),
(3, 'En attente', 50, '<span class="badge bg-warning">En attente</span>'),
(4, 'Terminé', 100, '<span class="badge bg-success">Terminé</span>'),
(5, 'Annulé', 0, '<span class="badge bg-danger">Annulé</span>');

-- =============================================
-- TODO_DETAIL_STATUTS (Statuts des sous-tâches)
-- =============================================
INSERT INTO todo_detail_statuts (id, statut, score, span_html) VALUES
(1, 'Non commencé', 0, '<span class="badge bg-light text-dark">Non commencé</span>'),
(2, 'En progression', 50, '<span class="badge bg-info">En progression</span>'),
(3, 'Complété', 100, '<span class="badge bg-success">Complété</span>'),
(4, 'Bloqué', 0, '<span class="badge bg-danger">Bloqué</span>');

-- =============================================
-- TODO_CATEGORIES (Catégories par utilisateur)
-- =============================================
-- Catégories pour john_doe (user_id = 2)
INSERT INTO todo_categories (id, id_user, nom, color) VALUES
(1, 2, 'Travail', '#3498db'),
(2, 2, 'Personnel', '#27ae60'),
(3, 2, 'Urgent', '#e74c3c'),
(4, 2, 'Projets', '#9b59b6');

-- Catégories pour jane_smith (user_id = 3)
INSERT INTO todo_categories (id, id_user, nom, color) VALUES
(5, 3, 'Réunions', '#f39c12'),
(6, 3, 'Développement', '#1abc9c'),
(7, 3, 'Documentation', '#34495e');

-- Catégories pour bob_wilson (user_id = 4)
INSERT INTO todo_categories (id, id_user, nom, color) VALUES
(8, 4, 'Études', '#e67e22'),
(9, 4, 'Loisirs', '#2ecc71');

-- =============================================
-- TODOS (Tâches principales)
-- =============================================
-- Tâches pour john_doe (user_id = 2)
INSERT INTO todos (id, id_user, id_categorie, id_statut, titre, description, date_creation, date_echeance) VALUES
(1, 2, 1, 2, 'Préparer la présentation client', 'Créer les slides pour la réunion de vendredi avec le client ABC', '2026-01-25 09:00:00', '2026-02-01 17:00:00'),
(2, 2, 1, 1, 'Réviser le code du module authentification', 'Revoir et optimiser le code du module auth selon les nouvelles normes de sécurité', '2026-01-26 10:30:00', '2026-02-05 18:00:00'),
(3, 2, 2, 4, 'Rendez-vous médecin', 'Consultation annuelle chez le médecin traitant', '2026-01-20 08:00:00', '2026-01-28 14:00:00'),
(4, 2, 3, 2, 'Correction bug critique', 'Bug sur le formulaire de connexion en production', '2026-01-29 11:00:00', '2026-01-30 12:00:00'),
(5, 2, 4, 1, 'Planifier le sprint 12', 'Définir les user stories et estimer les points', '2026-01-28 14:00:00', '2026-02-03 09:00:00');

-- Tâches pour jane_smith (user_id = 3)
INSERT INTO todos (id, id_user, id_categorie, id_statut, titre, description, date_creation, date_echeance) VALUES
(6, 3, 5, 3, 'Réunion hebdomadaire équipe', 'Point d''avancement avec l''équipe de développement', '2026-01-27 08:00:00', '2026-01-31 10:00:00'),
(7, 3, 6, 2, 'Développer API REST pour les todos', 'Implémenter les endpoints CRUD pour la gestion des todos', '2026-01-24 09:00:00', '2026-02-07 17:00:00'),
(8, 3, 7, 1, 'Rédiger la documentation technique', 'Documenter l''architecture et les APIs du projet MyTodo', '2026-01-29 13:00:00', '2026-02-14 17:00:00');

-- Tâches pour bob_wilson (user_id = 4)
INSERT INTO todos (id, id_user, id_categorie, id_statut, titre, description, date_creation, date_echeance) VALUES
(9, 4, 8, 2, 'Apprendre Spring Security', 'Suivre le cours en ligne sur Spring Security et JWT', '2026-01-22 10:00:00', '2026-02-15 23:59:00'),
(10, 4, 9, 1, 'Organiser sortie weekend', 'Planifier une randonnée pour le weekend prochain', '2026-01-28 19:00:00', '2026-02-08 08:00:00');

-- =============================================
-- TODO_DETAILS (Sous-tâches)
-- =============================================
-- Détails pour "Préparer la présentation client" (todo_id = 1)
INSERT INTO todo_details (id, id_todo, id_statut_detail, tache, heure_debut, heure_fin, priorite, avancement) VALUES
(1, 1, 3, 'Collecter les données du projet', '09:00', '11:00', 1, 100),
(2, 1, 2, 'Créer les graphiques', '11:00', '13:00', 2, 60),
(3, 1, 1, 'Rédiger le contenu des slides', '14:00', '16:00', 3, 0),
(4, 1, 1, 'Répéter la présentation', '16:00', '17:00', 4, 0);

-- Détails pour "Réviser le code du module authentification" (todo_id = 2)
INSERT INTO todo_details (id, id_todo, id_statut_detail, tache, heure_debut, heure_fin, priorite, avancement) VALUES
(5, 2, 1, 'Analyser le code existant', '09:00', '12:00', 1, 0),
(6, 2, 1, 'Identifier les vulnérabilités', '13:00', '15:00', 2, 0),
(7, 2, 1, 'Implémenter les corrections', '15:00', '18:00', 3, 0);

-- Détails pour "Correction bug critique" (todo_id = 4)
INSERT INTO todo_details (id, id_todo, id_statut_detail, tache, heure_debut, heure_fin, priorite, avancement) VALUES
(8, 4, 3, 'Reproduire le bug', '09:00', '09:30', 1, 100),
(9, 4, 2, 'Identifier la cause racine', '09:30', '10:30', 2, 75),
(10, 4, 1, 'Développer le correctif', '10:30', '11:30', 3, 0),
(11, 4, 1, 'Tester et déployer', '11:30', '12:00', 4, 0);

-- Détails pour "Développer API REST pour les todos" (todo_id = 7)
INSERT INTO todo_details (id, id_todo, id_statut_detail, tache, heure_debut, heure_fin, priorite, avancement) VALUES
(12, 7, 3, 'Créer les entités JPA', '09:00', '11:00', 1, 100),
(13, 7, 3, 'Implémenter les repositories', '11:00', '12:00', 2, 100),
(14, 7, 2, 'Développer les services', '13:00', '16:00', 3, 50),
(15, 7, 1, 'Créer les contrôleurs REST', '09:00', '12:00', 4, 0),
(16, 7, 1, 'Écrire les tests unitaires', '13:00', '17:00', 5, 0);

-- Détails pour "Apprendre Spring Security" (todo_id = 9)
INSERT INTO todo_details (id, id_todo, id_statut_detail, tache, heure_debut, heure_fin, priorite, avancement) VALUES
(17, 9, 3, 'Introduction à Spring Security', '10:00', '12:00', 1, 100),
(18, 9, 2, 'Configuration et filtres', '14:00', '16:00', 2, 40),
(19, 9, 1, 'JWT et authentification', '10:00', '12:00', 3, 0),
(20, 9, 1, 'OAuth2 et SSO', '14:00', '17:00', 4, 0);

-- =============================================
-- Mise à jour des séquences après insertion
-- =============================================
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('todo_statuts_id_seq', (SELECT MAX(id) FROM todo_statuts));
SELECT setval('todo_detail_statuts_id_seq', (SELECT MAX(id) FROM todo_detail_statuts));
SELECT setval('todo_categories_id_seq', (SELECT MAX(id) FROM todo_categories));
SELECT setval('todos_id_seq', (SELECT MAX(id) FROM todos));
SELECT setval('todo_details_id_seq', (SELECT MAX(id) FROM todo_details));
