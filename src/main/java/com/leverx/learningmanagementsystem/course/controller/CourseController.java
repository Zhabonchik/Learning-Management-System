package com.leverx.learningmanagementsystem.course.controller;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.facade.CourseWebFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
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

import static com.leverx.learningmanagementsystem.course.constants.CourseConstants.DEFAULT_COURSE_PAGE;
import static com.leverx.learningmanagementsystem.course.constants.CourseConstants.DEFAULT_COURSE_PAGE_SIZE;
import static com.leverx.learningmanagementsystem.course.constants.CourseConstants.DEFAULT_COURSE_SORT_TYPE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseWebFacade courseWebFacade;

    @GetMapping
    public PagedModel<CourseResponseDto> getAll(@PageableDefault(size = DEFAULT_COURSE_PAGE_SIZE,
            page = DEFAULT_COURSE_PAGE, sort = DEFAULT_COURSE_SORT_TYPE) Pageable pageable) {
        var page = courseWebFacade.getAll(pageable);
        return new PagedModel<>(page);
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
