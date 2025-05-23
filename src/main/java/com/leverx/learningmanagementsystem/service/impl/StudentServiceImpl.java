package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.exception.NotEnoughCoinsException;
import com.leverx.learningmanagementsystem.exception.StudentAlreadyEnrolledException;
import com.leverx.learningmanagementsystem.repository.StudentRepository;
import com.leverx.learningmanagementsystem.service.CourseService;
import com.leverx.learningmanagementsystem.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final CourseService courseService;
    private final StudentRepository studentRepository;

    @Override
    public Student getById(UUID id) {
        log.info("Get student [id = {}]", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found [id = {%s}]".formatted(id)));
    }

    @Override
    public List<Student> getAll() {
        log.info("Get all students");
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllByIdIn(List<UUID> ids) {
        List<Student> students = studentRepository.findAllByIdIn(ids);
        if (students.size() != ids.size()) {
            throw new IncorrectResultSizeException("Some of requested students don't exist");
        }
        return students;
    }

    @Override
    public Student create(Student student) {
        log.info("Create student: {}", student);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student update(Student student) {
        if (studentRepository.findById(student.getId()).isEmpty()) {
            throw new EntityNotFoundException("Student not found [id = {%s}]".formatted(student.getId()));
        }
        log.info("Update student [id = {}]", student.getId());
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void enrollForCourse(UUID studentId, UUID courseId) {
        Student student = getById(studentId);
        Course course = courseService.getById(courseId);
        BigDecimal coursePrice = course.getPrice();
        BigDecimal studentBalance = student.getCoins();

        validateCourseEnrollment(course, studentId);
        validateStudentBalance(studentBalance, coursePrice);

        transferCoins(course, student);

        course.getStudents().add(student);
        student.getCourses().add(course);
    }

    @Override
    public void deleteById(UUID id) {
        getById(id);
        log.info("Delete student [id = {}]", id);
        studentRepository.deleteById(id);
    }

    private void validateCourseEnrollment(Course course, UUID studentId) {
        if (isStudentEnrolledInCourse(course, studentId)) {
            throw new StudentAlreadyEnrolledException(
                    "Student already enrolled for course [studentId = {%s}, courseId = {%s}]"
                            .formatted(studentId, course.getId()));
        }
    }

    private boolean isStudentEnrolledInCourse(Course course, UUID studentId) {
        return course.getStudents().stream().anyMatch(student -> studentId.equals(student.getId()));
    }

    private void validateStudentBalance(BigDecimal studentBalance, BigDecimal coursePrice) {
        if (studentBalance.compareTo(coursePrice) < 0) {
            throw new NotEnoughCoinsException(
                    "Student doesn't have enough coins to enroll for course.");
        }
    }

    private void transferCoins(Course course, Student student) {
        student.setCoins(student.getCoins().subtract(course.getPrice()));
        course.setCoinsPaid(course.getCoinsPaid().add(course.getPrice()));
    }
}
