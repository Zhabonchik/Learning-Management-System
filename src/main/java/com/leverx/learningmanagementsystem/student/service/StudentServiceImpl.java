package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.course.dto.CourseId;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.student.dto.StudentId;
import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
import com.leverx.learningmanagementsystem.utils.exception.model.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.utils.exception.model.NotEnoughCoinsException;
import com.leverx.learningmanagementsystem.utils.exception.model.StudentAlreadyEnrolledException;
import com.leverx.learningmanagementsystem.student.repository.StudentRepository;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Student getByIdForUpdate(UUID id) {
        log.info("Get student for update [id = {}]", id);
        return studentRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found [id = {%s}]".formatted(id)));
    }


    @Override
    public List<Student> getAll() {
        log.info("Get all students");
        return studentRepository.findAll();
    }

    @Override
    public Page<Student> getAll(Pageable pageable) {
        log.info("Get all students on page {}, with page size {}", pageable.getPageNumber(), pageable.getPageSize());
        return studentRepository.findAll(pageable);
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
        log.info("Check if student exists [id = {}]", student.getId());
        getById(student.getId());

        log.info("Update student [id = {}]", student.getId());
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void enrollForCourse(StudentId studentId, CourseId courseId) {
        Student student = getByIdForUpdate(studentId.id());
        Course course = courseService.getByIdForUpdate(courseId.id());
        BigDecimal coursePrice = course.getPrice();
        BigDecimal studentBalance = student.getCoins();

        validateCourseEnrollment(course, studentId.id());
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
