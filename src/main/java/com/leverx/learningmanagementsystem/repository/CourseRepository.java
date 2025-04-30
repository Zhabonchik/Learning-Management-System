package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends CrudRepository<Course, UUID> {

    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    Optional<Course> findById(UUID id);

    @EntityGraph(attributePaths = {"lessons", "students", "settings"})
    List<Course> findAll();
}
