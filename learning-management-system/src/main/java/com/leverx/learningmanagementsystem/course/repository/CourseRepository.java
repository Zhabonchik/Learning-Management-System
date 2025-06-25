package com.leverx.learningmanagementsystem.course.repository;

import com.leverx.learningmanagementsystem.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface CourseRepository extends CrudRepository<Course, UUID> {

    @Override
    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    Optional<Course> findById(UUID id);

    @Lock(PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id")
    Optional<Course> findByIdForUpdate(UUID id);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons")
    List<Course> findAllWithLessons();

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
    List<Course> findAllWithStudents();

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.settings")
    List<Course> findAllWithSettings();

    @Query("SELECT c FROM Course c")
    Page<Course> findAll(Pageable pageable);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c IN :courses")
    List<Course> findAllWithLessons(@Param("courses") List<Course> courses);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.settings WHERE c IN :courses")
    List<Course> findAllWithSettings(@Param("courses") List<Course> courses);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students WHERE c IN :courses")
    List<Course> findAllWithStudents(@Param("courses") List<Course> courses);


    List<Course> findAllByIdIn(List<UUID> uuids);

    @EntityGraph(attributePaths = {"students", "settings"})
    List<Course> findAllBySettingsStartDateBetween(LocalDateTime tomorrowStart, LocalDateTime tomorrowEnd);
}
