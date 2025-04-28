package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.GetLessonDto;
import com.leverx.learningmanagementsystem.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public List<GetLessonDto> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public GetLessonDto getLesson(@PathVariable("id") UUID id) {
        return lessonService.getById(id);
    }

    @PostMapping
    public GetLessonDto create(@RequestBody CreateLessonDto createLessonDto) {
        return lessonService.create(createLessonDto);
    }

    @PutMapping("/{id}")
    public GetLessonDto update(@PathVariable("id") UUID id, @RequestBody CreateLessonDto createLessonDto) {
        return lessonService.update(id, createLessonDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        lessonService.delete(id);
    }
}
