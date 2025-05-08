package com.leverx.learningmanagementsystem.student.repository;

import com.leverx.learningmanagementsystem.student.model.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends CrudRepository<Student, UUID> {

    @Override
    @EntityGraph(attributePaths = "courses")
    Optional<Student> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = "courses")
    List<Student> findAll();

    List<Student> findAllByIdIn(List<UUID> uuids);
}
