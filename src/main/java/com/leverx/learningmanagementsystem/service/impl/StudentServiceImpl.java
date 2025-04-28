package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.GetStudentDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.mapper.student.StudentMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.StudentRepository;
import com.leverx.learningmanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository,
                              StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public GetStudentDto getById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return studentMapper.toGetStudentDto(student);
    }

    @Override
    public List<GetStudentDto> getAllStudents() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        return students.stream()
                .map(studentMapper::toGetStudentDto)
                .toList();
    }

    @Override
    public GetStudentDto create(CreateStudentDto createStudentDto) {
        Student student = studentMapper.toStudent(createStudentDto);
        Set<Course> courses = (Set<Course>) courseRepository.findAllById(createStudentDto.courseId());

        student.setCourses(courses);
        studentRepository.save(student);

        return studentMapper.toGetStudentDto(student);
    }

    @Override
    public GetStudentDto update(UUID id, CreateStudentDto updateStudentDto) {

        if (studentRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Student not found");
        }

        Student student = studentMapper.toStudent(updateStudentDto);
        Set<Course> courses = (Set<Course>) courseRepository.findAllById(updateStudentDto.courseId());

        student.setId(id);
        student.setCourses(courses);
        studentRepository.save(student);

        return studentMapper.toGetStudentDto(student);
    }

    @Override
    public void delete(UUID id) {
        studentRepository.deleteById(id);
    }
}
