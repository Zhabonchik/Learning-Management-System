package com.leverx.learningmanagementsystem.student.controller;

import com.leverx.learningmanagementsystem.course.dto.CourseId;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentId;
import com.leverx.learningmanagementsystem.student.dto.StudentResponseDto;
import com.leverx.learningmanagementsystem.student.webfacade.StudentWebFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private final StudentWebFacade studentWebFacade;

    @GetMapping
    public Page<StudentResponseDto> getAll(@PageableDefault(size = 3, page = 0, sort = "created") Pageable pageable) {
        return studentWebFacade.getAll(pageable);
    }

    @GetMapping("/{id}")
    public StudentResponseDto getById(@PathVariable("id") UUID id) {
        return studentWebFacade.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public StudentResponseDto create(@RequestBody @Valid CreateStudentDto createStudentDto) {
        return studentWebFacade.create(createStudentDto);
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    @ResponseStatus(CREATED)
    public void addCourse(@PathVariable("studentId") UUID studentId, @PathVariable("courseId") UUID courseId) {
        studentWebFacade.enrollForCourse(StudentId.of(studentId), CourseId.of(courseId));
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateById(@PathVariable("id") UUID id, @RequestBody @Valid CreateStudentDto updateStudentDto) {
        return studentWebFacade.updateById(id, updateStudentDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id) {
        studentWebFacade.deleteById(id);
    }
}
