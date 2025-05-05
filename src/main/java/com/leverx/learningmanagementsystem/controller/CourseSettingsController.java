package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
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
@RequestMapping("/course-settings")
@AllArgsConstructor
public class CourseSettingsController {

    private final CourseSettingsService courseSettingsService;
    private final CourseSettingsMapper courseSettingsMapper;

    @GetMapping
    public List<CourseSettingsResponseDto> getAll() {
        return courseSettingsMapper.toDtoList(courseSettingsService.getAll());
    }

    @GetMapping("/{id}")
    public CourseSettingsResponseDto getById(@PathVariable("id") UUID id) {
        return courseSettingsMapper.toDto(courseSettingsService.getById(id));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CourseSettingsResponseDto create(@RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsMapper.toDto(courseSettingsService.create(createCourseSettingsDto));
    }

    @PutMapping("/{id}")
    public CourseSettingsResponseDto updateById(@PathVariable("id") UUID id,
                                                @RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsMapper.toDto(courseSettingsService.update(id, createCourseSettingsDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        courseSettingsService.delete(id);
    }
}
