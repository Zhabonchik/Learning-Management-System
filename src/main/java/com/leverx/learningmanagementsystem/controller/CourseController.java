package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<GetCourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public GetCourseDto getCourse(@PathVariable("id") UUID id) {
        return courseService.getById(id);
    }

    @PostMapping
    public GetCourseDto addCourse(@RequestBody CreateCourseDto createCourseDto) {
        return courseService.create(createCourseDto);
    }

    @PutMapping("/{id}")
    public GetCourseDto updateCourse(@PathVariable("id") UUID id, @RequestBody CreateCourseDto updateCourseDto) {
        return courseService.update(id, updateCourseDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable("id") UUID id) {
        courseService.delete(id);
    }

}
