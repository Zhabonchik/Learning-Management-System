package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.course.dto.CourseId;
import com.leverx.learningmanagementsystem.student.dto.StudentId;
import com.leverx.learningmanagementsystem.student.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    Student getById(UUID id);

    Student getByIdForUpdate(UUID id);

    List<Student> getAll();

    List<Student> getAllByIdIn(List<UUID> ids);

    Student create(Student student);

    Student update(Student student);

    void enrollForCourse(StudentId studentId, CourseId courseId);

    void deleteById(UUID id);

}
