package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.mapper.course.CourseMapper;
import com.leverx.learningmanagementsystem.service.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping
    public List<GetCourseDto> getAllCourses() {
        return courseMapper.toGetCourseDtoList(courseService.getAll());
    }

    @GetMapping("/{id}")
    public GetCourseDto getCourse(@PathVariable("id") UUID id) {
        return courseMapper.toGetCourseDto(courseService.getById(id));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public GetCourseDto addCourse(@RequestBody @Valid CreateCourseDto createCourseDto) {
        return courseMapper.toGetCourseDto(courseService.create(createCourseDto));
    }

    @PutMapping("/{id}")
    public GetCourseDto updateCourse(@PathVariable("id") UUID id, @RequestBody @Valid CreateCourseDto updateCourseDto) {
        return courseMapper.toGetCourseDto(courseService.update(id, updateCourseDto));
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable("id") UUID id) {
        courseService.delete(id);
    }

}
