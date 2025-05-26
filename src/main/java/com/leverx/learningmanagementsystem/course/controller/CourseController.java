package com.leverx.learningmanagementsystem.course.controller;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.webfacade.CourseWebFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final String DEFAULT_PAGE = "0";
    private final String DEFAULT_PAGE_SIZE = "3";

    private final CourseWebFacade courseWebFacade;

    @GetMapping
    public Page<CourseResponseDto> getAll(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer pageNumber,
                                          @RequestParam(value = "page-size", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return courseWebFacade.getAll(pageable);
    }

    @GetMapping("/{id}")
    public CourseResponseDto getById(@PathVariable("id") UUID id) {
        return courseWebFacade.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CourseResponseDto create(@RequestBody @Valid CreateCourseDto createCourseDto) {
        return courseWebFacade.create(createCourseDto);
    }

    @PutMapping("/{id}")
    public CourseResponseDto updateById(@PathVariable("id") UUID id, @RequestBody @Valid CreateCourseDto updateCourseDto) {
        return courseWebFacade.updateById(id, updateCourseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id) {
        courseWebFacade.deleteById(id);
    }
}
