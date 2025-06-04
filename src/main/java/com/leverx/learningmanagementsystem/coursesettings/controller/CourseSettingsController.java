package com.leverx.learningmanagementsystem.coursesettings.controller;

import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.coursesettings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.coursesettings.facade.CourseSettingsWebFacade;
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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/course-settings")
@AllArgsConstructor
public class CourseSettingsController {

    private final CourseSettingsWebFacade courseSettingsWebFacade;

    @GetMapping
    public PagedModel<CourseSettingsResponseDto> getAll(@PageableDefault(size = 3, page = 0, sort = "created") Pageable pageable) {
        var page = courseSettingsWebFacade.getAll(pageable);
        return new PagedModel<>(page);
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
