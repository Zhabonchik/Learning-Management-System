package com.leverx.learningmanagementsystem.lesson.controller;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.webfacade.LessonWebFacade;
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
@RequestMapping("/lessons")
@AllArgsConstructor
public class LessonController {

    private final String DEFAULT_PAGE = "0";
    private final String DEFAULT_PAGE_SIZE = "3";

    private final LessonWebFacade lessonWebFacade;

    @GetMapping
    public Page<LessonResponseDto> getAll(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer pageNumber,
                                          @RequestParam(value = "page-size", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return  lessonWebFacade.getAll(pageable);
    }

    @GetMapping("/{id}")
    public LessonResponseDto getById(@PathVariable("id") UUID id) {
        return lessonWebFacade.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public LessonResponseDto create(@RequestBody @Valid CreateLessonDto createLessonDto) {
        return lessonWebFacade.create(createLessonDto);
    }

    @PutMapping("/{id}")
    public LessonResponseDto updateById(@PathVariable("id") UUID id, @RequestBody @Valid CreateLessonDto createLessonDto) {
        return lessonWebFacade.updateById(id, createLessonDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id) {
        lessonWebFacade.deleteById(id);
    }
}
