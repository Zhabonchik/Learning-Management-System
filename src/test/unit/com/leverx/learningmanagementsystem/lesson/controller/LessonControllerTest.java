package com.leverx.learningmanagementsystem.lesson.controller;

import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.webfacade.LessonWebFacade;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.LESSONS;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.NEW_VIDEO_LESSON_PLATFORM;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.NON_EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.TOTAL_NUMBER_OF_LESSONS;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.initializeLessonResponseDto;
import static com.leverx.learningmanagementsystem.utils.LessonTestUtils.initializePage;
import static com.leverx.learningmanagementsystem.utils.TestUtils.CREATED;
import static com.leverx.learningmanagementsystem.utils.TestUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE_SIZE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LessonController.class)
@Tag("unit")
public class LessonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LessonWebFacade lessonWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_ShouldReturnAllLessonsAnd200() throws Exception {
        // given
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_LESSONS, Sort.by(CREATED));
        Page<LessonResponseDto> page = initializePage(pageable);
        when(lessonWebFacade.getAll(pageable)).thenReturn(page);

        // when
        var response = mockMvc.perform(get(LESSONS)
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_LESSONS)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_LESSONS))
                .andExpect(jsonPath("$.content[0].platform").value(NEW_VIDEO_LESSON_PLATFORM))
                .andExpect(jsonPath("$.content[1].platform").value(NEW_VIDEO_LESSON_PLATFORM));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnLessonAnd200() throws Exception {
        // given
        LessonResponseDto responseDto = initializeLessonResponseDto();
        when(lessonWebFacade.getById(EXISTING_LESSON_ID)).thenReturn(responseDto);

        // when
        var response = mockMvc.perform(get(LESSONS + "/" + EXISTING_LESSON_ID));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_LESSON_ID.toString()))
                .andExpect(jsonPath("$.platform").value(NEW_VIDEO_LESSON_PLATFORM));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        // given
        when(lessonWebFacade.getById(NON_EXISTING_LESSON_ID))
                .thenThrow(new EntityNotFoundException(
                        "Lesson not found [id = {%s}]".formatted(NON_EXISTING_LESSON_ID))
                );

        // when
        var response = mockMvc.perform(get(LESSONS + "/" + NON_EXISTING_LESSON_ID));

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Lesson not found [id = {%s}]".formatted(NON_EXISTING_LESSON_ID)
                ));
    }

}
