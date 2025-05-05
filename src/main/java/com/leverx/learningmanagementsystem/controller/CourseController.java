package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.course.CourseResponseDto;
import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.mapper.course.CourseMapper;
import com.leverx.learningmanagementsystem.service.CourseService;
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
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping
    public List<CourseResponseDto> getAll() {
        return courseMapper.toDtos(courseService.getAll());
    }

    @GetMapping("/{id}")
    public CourseResponseDto getById(@PathVariable("id") UUID id) {
        return courseMapper.toDto(courseService.getById(id));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CourseResponseDto create(@RequestBody @Valid CreateCourseDto createCourseDto) {
        return courseMapper.toDto(courseService.create(createCourseDto));
    }

    @PutMapping("/{id}")
    public CourseResponseDto updateById(@PathVariable("id") UUID id, @RequestBody @Valid CreateCourseDto updateCourseDto) {
        return courseMapper.toDto(courseService.updateById(id, updateCourseDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id) {
        courseService.deleteById(id);
    }

}
