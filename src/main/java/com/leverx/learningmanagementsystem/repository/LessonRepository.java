package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.Lesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, UUID> {
}
