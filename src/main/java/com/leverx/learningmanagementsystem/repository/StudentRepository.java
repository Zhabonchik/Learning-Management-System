package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends CrudRepository<Student, UUID> {

    @EntityGraph(attributePaths = "courses")
    Optional<Student> findById(UUID id);

    @EntityGraph(attributePaths = "courses")
    List<Student> findAll();

    @Query("SELECT s FROM Student s WHERE s.id in :ids")
    List<Student> findAllById(@Param("ids") List<UUID> ids);
}
