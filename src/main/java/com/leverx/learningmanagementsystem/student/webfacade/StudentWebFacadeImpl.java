package com.leverx.learningmanagementsystem.student.webfacade;

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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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
    public StudentResponseDto getById(UUID id) {
        Student student = studentService.getById(id);
        return studentMapper.toDto(student);
    }

    @Override
    public StudentResponseDto create(CreateStudentDto createStudentDto) {
        Student student = studentMapper.toModel(createStudentDto);

        constructStudent(student, createStudentDto);

        Student createdStudent = studentService.create(student);
        return studentMapper.toDto(createdStudent);
    }

    @Override
    public void enrollForCourse(StudentId studentId, CourseId courseId) {
        studentService.enrollForCourse(studentId, courseId);
    }

    @Override
    public StudentResponseDto updateById(UUID id, CreateStudentDto createStudentDto) {
        Student student = studentMapper.toModel(createStudentDto);

        constructStudent(student, createStudentDto);
        student.setId(id);

        Student updatedStudent = studentService.update(student);
        return studentMapper.toDto(updatedStudent);
    }

    @Override
    public void deleteById(UUID id) {
        studentService.deleteById(id);
    }

    private void constructStudent(Student student, CreateStudentDto createStudentDto) {
        List<Course> courses = courseService.getAllByIdIn(createStudentDto.courseIds());
        student.setCourses(courses);

        courses.forEach(course -> {
            if (!course.getStudents().contains(student)) {
                course.getStudents().add(student);
            }
        });
    }
}
