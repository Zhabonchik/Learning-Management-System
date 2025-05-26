package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.course.dto.CourseId;
import com.leverx.learningmanagementsystem.student.dto.StudentId;
import com.leverx.learningmanagementsystem.student.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    Student getById(UUID id);

    Student getByIdForUpdate(UUID id);

    List<Student> getAll();

    Page<Student> getAll(Pageable pageable);

    List<Student> getAllByIdIn(List<UUID> ids);

    Student create(Student student);

    Student update(Student student);

    void enrollForCourse(StudentId studentId, CourseId courseId);

    void deleteById(UUID id);

}
