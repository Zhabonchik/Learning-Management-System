package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.GetStudentDto;
import com.leverx.learningmanagementsystem.mapper.student.StudentMapper;
import com.leverx.learningmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @GetMapping
    public List<GetStudentDto> getAllStudents() {
        return studentMapper.toGetStudentDtoList(studentService.getAll());
    }

    @GetMapping("/{id}")
    public GetStudentDto getStudent(@PathVariable("id") UUID id) {
        return studentMapper.toGetStudentDto(studentService.getById(id));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public GetStudentDto addStudent(@RequestBody @Valid CreateStudentDto createStudentDto) {
        return studentMapper.toGetStudentDto(studentService.create(createStudentDto));
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    @ResponseStatus(CREATED)
    public void addCourse(@PathVariable("studentId") UUID studentId, @PathVariable("courseId") UUID courseId) {
        studentService.enrollForCourse(studentId, courseId);
    }

    @PutMapping("/{id}")
    public GetStudentDto updateStudent(@PathVariable("id") UUID id, @RequestBody @Valid CreateStudentDto updateStudentDto) {
        return studentMapper.toGetStudentDto(studentService.update(id, updateStudentDto));
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") UUID id) {
        studentService.delete(id);
    }
}
