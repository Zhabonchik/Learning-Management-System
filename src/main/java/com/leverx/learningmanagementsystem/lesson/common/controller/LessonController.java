package com.leverx.learningmanagementsystem.lesson.common.controller;

import com.leverx.learningmanagementsystem.lesson.common.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.common.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.common.facade.LessonWebFacade;
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

import static com.leverx.learningmanagementsystem.lesson.constants.LessonConstants.DEFAULT_LESSON_PAGE;
import static com.leverx.learningmanagementsystem.lesson.constants.LessonConstants.DEFAULT_LESSON_PAGE_SIZE;
import static com.leverx.learningmanagementsystem.lesson.constants.LessonConstants.DEFAULT_LESSON_SORT_TYPE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/lessons")
@AllArgsConstructor
public class LessonController {

    private final LessonWebFacade lessonWebFacade;

    @GetMapping
    public PagedModel<LessonResponseDto> getAll(@PageableDefault(size = DEFAULT_LESSON_PAGE_SIZE,
            page = DEFAULT_LESSON_PAGE, sort = DEFAULT_LESSON_SORT_TYPE) Pageable pageable) {
        var page = lessonWebFacade.getAll(pageable);
        return new PagedModel<>(page);
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
