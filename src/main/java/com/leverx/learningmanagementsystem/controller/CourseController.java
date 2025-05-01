package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService service;

    @Autowired
    public CourseController(CourseService courseService) {
        this.service = courseService;
    }

    @GetMapping
    public List<GetCourseDto> getAllCourses() {
        return service.getAllCourses();
    }

    @GetMapping("/{id}")
    public GetCourseDto getCourse(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public GetCourseDto addCourse(@RequestBody @Valid CreateCourseDto createCourseDto) {
        return service.create(createCourseDto);
    }

    @PutMapping("/{id}")
    public GetCourseDto updateCourse(@PathVariable("id") UUID id, @RequestBody @Valid CreateCourseDto updateCourseDto) {
        return service.update(id, updateCourseDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable("id") UUID id) {
        service.delete(id);
    }

}
