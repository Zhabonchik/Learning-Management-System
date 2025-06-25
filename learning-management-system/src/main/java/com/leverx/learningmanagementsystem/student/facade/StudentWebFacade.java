package com.leverx.learningmanagementsystem.student.facade;

import com.leverx.learningmanagementsystem.course.dto.CourseId;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentId;
import com.leverx.learningmanagementsystem.student.dto.StudentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StudentWebFacade {

    List<StudentResponseDto> getAll();

    Page<StudentResponseDto> getAll(Pageable pageable);

    StudentResponseDto getById(UUID id);

    StudentResponseDto create(CreateStudentDto createStudentDto);

    void enrollForCourse(StudentId studentId, CourseId courseId);

    StudentResponseDto updateById(UUID id, CreateStudentDto createStudentDto);

    void deleteById(UUID id);

}
