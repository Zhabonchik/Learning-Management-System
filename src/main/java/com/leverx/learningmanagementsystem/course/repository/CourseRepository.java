package com.leverx.learningmanagementsystem.course.repository;

import com.leverx.learningmanagementsystem.course.model.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends CrudRepository<Course, UUID> {

    @Override
    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    Optional<Course> findById(UUID id);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons")
    List<Course> findAllWithLessons();

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
    List<Course> findAllWithStudents();

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.settings")
    List<Course> findAllWithSettings();

    List<Course> findAllByIdIn(List<UUID> uuids);

    @EntityGraph(attributePaths = {"students", "settings"})
    List<Course> findAllBySettingsStartDateBetween(LocalDateTime tomorrowStart, LocalDateTime tomorrowEnd);
}
