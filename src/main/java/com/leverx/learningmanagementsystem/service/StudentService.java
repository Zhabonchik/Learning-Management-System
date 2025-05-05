package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.entity.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    Student getById(UUID id);

    List<Student> getAll();

    Student create(CreateStudentDto createStudentDto);

    Student updateById(UUID id, CreateStudentDto updateStudentDto);

    void enrollForCourse(UUID studentId, UUID courseId);

    void deleteById(UUID id);

}
