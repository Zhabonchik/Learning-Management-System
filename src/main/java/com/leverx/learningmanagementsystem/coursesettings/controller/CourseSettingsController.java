package com.leverx.learningmanagementsystem.coursesettings.controller;

import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.coursesettings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.coursesettings.webfacade.CourseSettingsWebFacade;
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
@RequestMapping("/course-settings")
@AllArgsConstructor
public class CourseSettingsController {

    private final String DEFAULT_PAGE = "0";
    private final String DEFAULT_PAGE_SIZE = "3";

    private final CourseSettingsWebFacade courseSettingsWebFacade;

    @GetMapping
    public Page<CourseSettingsResponseDto> getAll(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer pageNumber,
                                                  @RequestParam(value = "page-size", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return courseSettingsWebFacade.getAll(pageable);
    }

    @GetMapping("/{id}")
    public CourseSettingsResponseDto getById(@PathVariable("id") UUID id) {
        return courseSettingsWebFacade.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CourseSettingsResponseDto create(@RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsWebFacade.create(createCourseSettingsDto);
    }

    @PutMapping("/{id}")
    public CourseSettingsResponseDto updateById(@PathVariable("id") UUID id,
                                                @RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsWebFacade.updateById(id, createCourseSettingsDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id) {
        courseSettingsWebFacade.deleteById(id);
    }
}
