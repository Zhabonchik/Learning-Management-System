package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.GetLessonDto;
import com.leverx.learningmanagementsystem.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService service;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.service = lessonService;
    }

    @GetMapping
    public List<GetLessonDto> getAllLessons() {
        return service.getAllLessons();
    }

    @GetMapping("/{id}")
    public GetLessonDto getLesson(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public GetLessonDto addLesson(@RequestBody @Valid CreateLessonDto createLessonDto) {
        return service.create(createLessonDto);
    }

    @PutMapping("/{id}")
    public GetLessonDto updateLesson(@PathVariable("id") UUID id, @RequestBody @Valid CreateLessonDto createLessonDto) {
        return service.update(id, createLessonDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
