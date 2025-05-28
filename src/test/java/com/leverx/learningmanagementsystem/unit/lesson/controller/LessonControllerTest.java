package com.leverx.learningmanagementsystem.unit.lesson.controller;

import com.leverx.learningmanagementsystem.lesson.controller.LessonController;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.webfacade.LessonWebFacade;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testutils.LessonTestUtils.EXISTING_LESSON_ID;
import static testutils.LessonTestUtils.NEW_VIDEO_LESSON_PLATFORM;
import static testutils.LessonTestUtils.NON_EXISTING_LESSON_ID;
import static testutils.LessonTestUtils.TOTAL_NUMBER_OF_LESSONS;
import static testutils.LessonTestUtils.initializeLessonResponseDto;
import static testutils.LessonTestUtils.initializePage;
import static testutils.TestUtils.DEFAULT_PAGE;
import static testutils.TestUtils.PAGE;
import static testutils.TestUtils.PAGE_SIZE;

@WebMvcTest(controllers = LessonController.class)
@Tag("unit-test")
public class LessonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LessonWebFacade lessonWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_ShouldReturnAllLessonsAnd200() throws Exception {
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_LESSONS);
        Page<LessonResponseDto> page = initializePage(pageable);
        Mockito.when(lessonWebFacade.getAll(pageable)).thenReturn(page);

        var response = mockMvc.perform(get("/lessons")
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_LESSONS)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_LESSONS))
                .andExpect(jsonPath("$.content[0].platform").value(NEW_VIDEO_LESSON_PLATFORM))
                .andExpect(jsonPath("$.content[1].platform").value(NEW_VIDEO_LESSON_PLATFORM));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnLessonAnd200() throws Exception {
        LessonResponseDto responseDto = initializeLessonResponseDto();
        Mockito.when(lessonWebFacade.getById(EXISTING_LESSON_ID)).thenReturn(responseDto);

        var response = mockMvc.perform(get("/lessons/" + EXISTING_LESSON_ID));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_LESSON_ID.toString()))
                .andExpect(jsonPath("$.platform").value(NEW_VIDEO_LESSON_PLATFORM));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        Mockito.when(lessonWebFacade.getById(NON_EXISTING_LESSON_ID))
                .thenThrow(new EntityNotFoundException(
                        "Lesson not found [id = {%s}]".formatted(NON_EXISTING_LESSON_ID))
        );

        var response = mockMvc.perform(get("/lessons/" + NON_EXISTING_LESSON_ID));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Lesson not found [id = {%s}]".formatted(NON_EXISTING_LESSON_ID)
                ));
    }

}
