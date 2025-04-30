package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends CrudRepository<Course, UUID> {

    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    Optional<Course> findById(UUID id);

    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    List<Course> findAll();

    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    @Query(value = "SELECT c FROM Course c JOIN c.settings s where s.startDate BETWEEN :tomorrowStart AND :tomorrowEnd")
    List<Course> findAllStartingTheFollowingDay(@Param("tomorrowStart") LocalDateTime tomorrowStart,
                                                @Param("tomorrowEnd") LocalDateTime tomorrowEnd);
}
