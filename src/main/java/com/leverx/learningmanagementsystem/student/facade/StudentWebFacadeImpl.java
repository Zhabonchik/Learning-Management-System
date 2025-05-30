package com.leverx.learningmanagementsystem.student.facade;

import com.leverx.learningmanagementsystem.course.dto.CourseId;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentId;
import com.leverx.learningmanagementsystem.student.dto.StudentResponseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.student.mapper.StudentMapper;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class StudentWebFacadeImpl implements StudentWebFacade {

    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentResponseDto> getAll() {
        List<Student> students = studentService.getAll();
        return studentMapper.toDtos(students);
    }

    @Override
    public Page<StudentResponseDto> getAll(Pageable pageable) {
        Page<Student> students = studentService.getAll(pageable);
        return students.map(studentMapper::toDto);
    }

    @Override
    public StudentResponseDto getById(UUID id) {
        var student = studentService.getById(id);
        return studentMapper.toDto(student);
    }

    @Override
    @Transactional
    public StudentResponseDto create(CreateStudentDto createStudentDto) {
        var student = studentMapper.toModel(createStudentDto);

        updateCourses(student, createStudentDto);

        var createdStudent = studentService.create(student);
        return studentMapper.toDto(createdStudent);
    }

    @Override
    public void enrollForCourse(StudentId studentId, CourseId courseId) {
        studentService.enrollForCourse(studentId, courseId);
    }

    @Override
    @Transactional
    public StudentResponseDto updateById(UUID id, CreateStudentDto createStudentDto) {
        var student = studentService.getById(id);

        update(student, createStudentDto);
        updateCourses(student, createStudentDto);

        var updatedStudent = studentService.update(student);
        return studentMapper.toDto(updatedStudent);
    }

    @Override
    public void deleteById(UUID id) {
        studentService.deleteById(id);
    }

    private void update(Student student, CreateStudentDto createStudentDto) {
        student.setFirstName(createStudentDto.firstName());
        student.setLastName(createStudentDto.lastName());
        student.setEmail(createStudentDto.email());
        student.setDateOfBirth(createStudentDto.dateOfBirth());
        student.setCoins(createStudentDto.coins());
        student.setLocale(createStudentDto.locale());
    }

    private void updateCourses(Student student, CreateStudentDto createStudentDto) {
        List<Course> courses = (isNull(createStudentDto.courseIds()))
                ? Collections.emptyList()
                : courseService.getAllByIdIn(createStudentDto.courseIds());

        replaceCourses(student, courses);
    }

    private void replaceCourses(Student student, List<Course> courses) {
        if (!isNull(student.getCourses())) {
            student.getCourses().forEach(course -> course.getStudents().remove(student));
            student.getCourses().clear();
        } else {
            student.setCourses(new ArrayList<>());
        }

        courses.forEach(course -> {
            course.getStudents().add(student);
            student.getCourses().add(course);
        });
    }
}
