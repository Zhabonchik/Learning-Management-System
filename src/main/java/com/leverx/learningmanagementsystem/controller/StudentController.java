package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.StudentResponseDto;
import com.leverx.learningmanagementsystem.webfacade.StudentWebFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private final StudentWebFacade studentWebFacade;

    @GetMapping
    public List<StudentResponseDto> getAll() {
        return studentWebFacade.getAll();
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
        studentWebFacade.enrollForCourse(studentId, courseId);
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
