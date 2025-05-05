package com.leverx.learningmanagementsystem.controller;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.LessonResponseDto;
import com.leverx.learningmanagementsystem.mapper.lesson.LessonMapper;
import com.leverx.learningmanagementsystem.service.LessonService;
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
@RequestMapping("/lessons")
@AllArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @GetMapping
    public List<LessonResponseDto> getAll() {
        return lessonMapper.toDtoList(lessonService.getAll());
    }

    @GetMapping("/{id}")
    public LessonResponseDto getById(@PathVariable("id") UUID id) {
        return lessonMapper.toDto(lessonService.getById(id));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public LessonResponseDto create(@RequestBody @Valid CreateLessonDto createLessonDto) {
        return lessonMapper.toDto(lessonService.create(createLessonDto));
    }

    @PutMapping("/{id}")
    public LessonResponseDto updateById(@PathVariable("id") UUID id, @RequestBody @Valid CreateLessonDto createLessonDto) {
        return lessonMapper.toDto(lessonService.update(id, createLessonDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        lessonService.delete(id);
    }
}
