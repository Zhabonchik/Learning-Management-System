package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.GetLessonDto;
import com.leverx.learningmanagementsystem.mapper.lesson.LessonMapper;
import com.leverx.learningmanagementsystem.service.LessonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/lessons")
@AllArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @GetMapping
    public List<GetLessonDto> getAllLessons() {
        return lessonMapper.toGetLessonDtoList(lessonService.getAll());
    }

    @GetMapping("/{id}")
    public GetLessonDto getLesson(@PathVariable("id") UUID id) {
        return lessonMapper.toGetLessonDto(lessonService.getById(id));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public GetLessonDto addLesson(@RequestBody @Valid CreateLessonDto createLessonDto) {
        return  lessonMapper.toGetLessonDto(lessonService.create(createLessonDto));
    }

    @PutMapping("/{id}")
    public GetLessonDto updateLesson(@PathVariable("id") UUID id, @RequestBody @Valid CreateLessonDto createLessonDto) {
        return  lessonMapper.toGetLessonDto(lessonService.update(id, createLessonDto));
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable("id") UUID id) {
        lessonService.delete(id);
    }
}
