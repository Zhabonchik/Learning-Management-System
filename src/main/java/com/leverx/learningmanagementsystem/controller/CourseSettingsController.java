package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/course-settings")
public class CourseSettingsController {

    private final CourseSettingsService courseSettingsService;

    @Autowired
    public CourseSettingsController(CourseSettingsService courseSettingsService) {
        this.courseSettingsService = courseSettingsService;
    }

    @GetMapping
    public List<GetCourseSettingsDto> getAllCourseSettings() {
        return courseSettingsService.getAllCourseSettings();
    }

    @GetMapping("/{id}")
    public GetCourseSettingsDto getCourseSettings(@PathVariable("id") UUID id) {
        return courseSettingsService.getById(id);
    }

    @PostMapping
    public GetCourseSettingsDto addCourseSettings(@RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsService.create(createCourseSettingsDto);
    }

    @PutMapping("/{id}")
    public GetCourseSettingsDto updateCourseSettings(@PathVariable("id") UUID id,
                                                     @RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsService.update(id, createCourseSettingsDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCourseSettings(@PathVariable("id") UUID id) {
        courseSettingsService.delete(id);
    }
}
