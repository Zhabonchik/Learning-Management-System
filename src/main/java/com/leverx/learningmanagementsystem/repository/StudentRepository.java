package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends CrudRepository<Student, UUID> {

    @EntityGraph(attributePaths = "courses")
    Optional<Student> findById(UUID id);

    @EntityGraph(attributePaths = "courses")
    List<Student> findAll();

}
