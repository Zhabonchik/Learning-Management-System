package com.leverx.learningmanagementsystem.student.repository;

import com.leverx.learningmanagementsystem.student.model.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface StudentRepository extends CrudRepository<Student, UUID> {

    @Override
    @EntityGraph(attributePaths = "courses")
    Optional<Student> findById(UUID id);

    @Lock(PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id")
    Optional<Student> findByIdForUpdate(UUID id);

    @Override
    @EntityGraph(attributePaths = "courses")
    List<Student> findAll();

    List<Student> findAllByIdIn(List<UUID> uuids);
}
