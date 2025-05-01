package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.GetStudentDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.mapper.student.StudentMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.StudentRepository;
import com.leverx.learningmanagementsystem.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;

    @Override
    public GetStudentDto getById(UUID id) {
        log.info("Get student by id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + id + " not found"));
        return studentMapper.toGetStudentDto(student);
    }

    @Override
    public List<GetStudentDto> getAllStudents() {
        log.info("Get all students");
        return studentMapper.toGetStudentDtoList(studentRepository.findAll());
    }

    @Override
    @Transactional
    public GetStudentDto create(CreateStudentDto createStudentDto) {

        Student student = studentMapper.toStudent(createStudentDto);
        log.info("Create student: {}", student);
        saveStudent(student, createStudentDto);

        return studentMapper.toGetStudentDto(student);
    }

    @Override
    @Transactional
    public GetStudentDto update(UUID id, CreateStudentDto updateStudentDto) {

        if (studentRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Student with id " + id + " not found");
        }

        Student student = studentMapper.toStudent(updateStudentDto);
        student.setId(id);

        log.info("Update student: {}", student);
        saveStudent(student, updateStudentDto);

        return studentMapper.toGetStudentDto(student);
    }

    @Override
    public void delete(UUID id) {
        log.info("Delete student: {}", id);
        studentRepository.deleteById(id);
    }

    private void saveStudent(Student student, CreateStudentDto createStudentDto) {

        log.info("Fetching courses for student with id = {}", student.getId());
        Set<Course> courses = new HashSet<>(courseRepository.findAllById(createStudentDto.courseIds()));

        if (courses.size() != createStudentDto.courseIds().size()) {
            throw new IncorrectResultSizeException("Some of requested courses don't exist");
        }

        student.setCourses(courses);
        studentRepository.save(student);
    }
}
