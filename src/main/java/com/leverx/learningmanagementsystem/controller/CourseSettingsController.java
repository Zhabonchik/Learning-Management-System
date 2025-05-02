package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/course-settings")
@AllArgsConstructor
public class CourseSettingsController {

    private final CourseSettingsService courseSettingsService;
    private final CourseSettingsMapper courseSettingsMapper;

    @GetMapping
    public List<GetCourseSettingsDto> getAll() {
        return courseSettingsMapper.toGetCourseSettingsDtoList(courseSettingsService.getAll());
    }

    @GetMapping("/{id}")
    public GetCourseSettingsDto getById(@PathVariable("id") UUID id) {
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettingsService.getById(id));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public GetCourseSettingsDto create(@RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettingsService.create(createCourseSettingsDto));
    }

    @PutMapping("/{id}")
    public GetCourseSettingsDto update(@PathVariable("id") UUID id,
                                                     @RequestBody @Valid CreateCourseSettingsDto createCourseSettingsDto) {
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettingsService.update(id, createCourseSettingsDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        courseSettingsService.delete(id);
    }
}
