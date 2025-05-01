package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.GetStudentDto;
import com.leverx.learningmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService studentService) {
        this.service = studentService;
    }

    @GetMapping
    public List<GetStudentDto> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public GetStudentDto getStudent(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public GetStudentDto addStudent(@RequestBody @Valid CreateStudentDto createStudentDto) {
        return service.create(createStudentDto);
    }

    @PutMapping("/{id}")
    public GetStudentDto updateStudent(@PathVariable("id") UUID id, @RequestBody @Valid CreateStudentDto updateStudentDto) {
        return service.update(id, updateStudentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
