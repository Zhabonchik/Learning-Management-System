package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.GetStudentDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.MismatchException;
import com.leverx.learningmanagementsystem.mapper.student.StudentMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.StudentRepository;
import com.leverx.learningmanagementsystem.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
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
        log.info("Get student by id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + id + " not found"));
        return studentMapper.toGetStudentDto(student);
    }

    @Override
    public List<GetStudentDto> getAllStudents() {
        log.info("Get all students");
        List<Student> students = (List<Student>) studentRepository.findAll();
        return students.stream()
                .map(studentMapper::toGetStudentDto)
                .toList();
    }

    @Override
    public GetStudentDto create(CreateStudentDto createStudentDto) {

        Student student = studentMapper.toStudent(createStudentDto);
        log.info("Create student: {}", student);
        saveStudent(student, createStudentDto);

        return studentMapper.toGetStudentDto(student);
    }

    @Override
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

        log.info("Fetching courses of student with id = {}", student.getId());
        Set<Course> courses = StreamSupport.stream(courseRepository
                        .findAllById(createStudentDto.courseId())
                        .spliterator(), false)
                .collect(Collectors.toSet());

        if (courses.size() != createStudentDto.courseId().size()) {
            throw new MismatchException("Numbers of courses of student with id = " + student.getId()
                    + " and requested courses mismatch (" + courses.size() + " != " + createStudentDto.courseId().size() + ")");
        }

        student.setCourses(courses);

        studentRepository.save(student);
    }
}
