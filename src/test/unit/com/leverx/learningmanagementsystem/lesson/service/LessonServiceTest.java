package com.leverx.learningmanagementsystem.lesson.service;

import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.lesson.repository.LessonRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.NEW_LESSON_TITLE;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.TOTAL_NUMBER_OF_LESSONS;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.initializeLesson;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    LessonRepository lessonRepository;

    @InjectMocks
    LessonServiceImpl lessonService;

    @Test
    @Tag("unit")
    void getAll_shouldReturnAllLessons() {
        // given
        Lesson firstLesson = initializeLesson();
        Lesson secondLesson = initializeLesson();
        when(lessonRepository.findAll()).thenReturn(List.of(firstLesson, secondLesson));

        // when
        var lessons = lessonService.getAll();

        // then
        assertAll(
                () -> assertEquals(TOTAL_NUMBER_OF_LESSONS, lessons.size()),
                () -> assertEquals(NEW_LESSON_TITLE, lessons.get(0).getTitle()),
                () -> assertEquals(NEW_LESSON_TITLE, lessons.get(1).getTitle())
        );
    }

    @Test
    @Tag("unit")
    void getById_givenId_shouldReturnLesson() {
        // given
        Lesson existingLesson = initializeLesson();
        existingLesson.setId(EXISTING_LESSON_ID);
        when(lessonRepository.findById(EXISTING_LESSON_ID)).thenReturn(Optional.of(existingLesson));

        // when
        var lesson = lessonService.getById(EXISTING_LESSON_ID);

        // then
        assertEquals(EXISTING_LESSON_ID, lesson.getId());
    }

    @Test
    @Tag("unit")
    void create_givenLesson_shouldReturnLesson() {
        // given
        Lesson newLesson = initializeLesson();
        when(lessonRepository.save(any(Lesson.class))).thenReturn(newLesson);

        // when
        var lesson = lessonService.create(newLesson);

        // then
        assertEquals(NEW_LESSON_TITLE, lesson.getTitle());
    }

}
