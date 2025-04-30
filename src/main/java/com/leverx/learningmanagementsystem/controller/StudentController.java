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

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<GetStudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public GetStudentDto getStudent(@PathVariable("id") UUID id) {
        return studentService.getById(id);
    }

    @PostMapping
    public GetStudentDto addStudent(@RequestBody @Valid CreateStudentDto createStudentDto) {
        return studentService.create(createStudentDto);
    }

    @PutMapping("/{id}")
    public GetStudentDto updateStudent(@PathVariable("id") UUID id, @RequestBody @Valid CreateStudentDto updateStudentDto) {
        return studentService.update(id, updateStudentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") UUID id) {
        studentService.delete(id);
    }
}
