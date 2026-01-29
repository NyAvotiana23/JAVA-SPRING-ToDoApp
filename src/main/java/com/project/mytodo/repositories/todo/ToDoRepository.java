package com.project.mytodo.repositories.todo;

import com.project.mytodo.entities.todo.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    List<ToDo> findByUserId(Long userId);

    List<ToDo> findByUserIdAndCategorieId(Long userId, Long categorieId);

    List<ToDo> findByUserIdAndStatutId(Long userId, Long statutId);

    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.titre LIKE %:titre%")
    List<ToDo> searchByTitre(@Param("userId") Long userId, @Param("titre") String titre);

    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.dateEcheance BETWEEN :start AND :end")
    List<ToDo> findByUserIdAndDateEcheanceBetween(@Param("userId") Long userId,
                                                   @Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);

    Optional<ToDo> findByIdAndUserId(Long id, Long userId);
}
