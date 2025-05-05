package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends CrudRepository<Course, UUID> {

    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    Optional<Course> findById(UUID id);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons")
    List<Course> findAllWithLessons();

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
    List<Course> findAllWithStudents();

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.settings")
    List<Course> findAllWithSettings();

    @Query("SELECT c FROM Course c WHERE c.id IN :ids")
    List<Course> findAllById(@Param("ids") List<UUID> ids);

    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    @Query(value = "SELECT c FROM Course c JOIN c.settings s where s.startDate BETWEEN :tomorrowStart AND :tomorrowEnd")
    List<Course> findAllStartingTheFollowingDay(@Param("tomorrowStart") LocalDateTime tomorrowStart,
                                                @Param("tomorrowEnd") LocalDateTime tomorrowEnd);
}
