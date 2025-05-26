package com.leverx.learningmanagementsystem.lesson.repository;

import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends CrudRepository<Lesson, UUID> {

    @EntityGraph(attributePaths = "course")
    Optional<Lesson> findById(UUID id);

    @EntityGraph(attributePaths = "course")
    List<Lesson> findAll();

    @EntityGraph(attributePaths = "course")
    Page<Lesson> findAll(Pageable pageable);

    List<Lesson> findAllByIdIn(List<UUID> uuids);
}
