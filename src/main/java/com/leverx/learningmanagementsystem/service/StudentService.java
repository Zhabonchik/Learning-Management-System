package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.GetStudentDto;
import com.leverx.learningmanagementsystem.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface StudentService {

    Student getEntityById(UUID id);

    GetStudentDto getById(UUID id);

    List<GetStudentDto> getAllStudents();

    GetStudentDto create(CreateStudentDto createStudentDto);

    GetStudentDto update(UUID id, CreateStudentDto updateStudentDto);

    void enrollForCourse(UUID studentId, UUID courseId);

    void delete(UUID id);

}
