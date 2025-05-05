package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends CrudRepository<Lesson, UUID> {

    @EntityGraph(attributePaths = "course")
    Optional<Lesson> findById(UUID id);

    @EntityGraph(attributePaths = "course")
    List<Lesson> findAll();

    @Query("SELECT l FROM Lesson l WHERE l.id IN :ids")
    List<Lesson> findAllById(@Param("ids") List<UUID> ids);
}
