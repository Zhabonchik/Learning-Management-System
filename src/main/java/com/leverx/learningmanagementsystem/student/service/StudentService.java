package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.student.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    Student getById(UUID id);

    List<Student> getAll();

    List<Student> getAllByIdIn(List<UUID> ids);

    Student create(Student student);

    Student update(Student student);

    void enrollForCourse(UUID studentId, UUID courseId);

    void deleteById(UUID id);

}
