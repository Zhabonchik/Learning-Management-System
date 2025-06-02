package com.leverx.learningmanagementsystem.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.AbstractIT;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.leverx.learningmanagementsystem.utils.LessonITUtils;

import static com.leverx.learningmanagementsystem.utils.LessonITUtils.EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.utils.LessonITUtils.EXISTING_LESSON_TITLE;
import static com.leverx.learningmanagementsystem.utils.LessonITUtils.LESSONS;
import static com.leverx.learningmanagementsystem.utils.LessonITUtils.NEW_LESSON_COURSE_ID;
import static com.leverx.learningmanagementsystem.utils.LessonITUtils.NEW_LESSON_TITLE;
import static com.leverx.learningmanagementsystem.utils.LessonITUtils.NON_EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.utils.LessonITUtils.NUMBER_OF_LESSON_FIELDS;
import static com.leverx.learningmanagementsystem.utils.LessonITUtils.TOTAL_NUMBER_OF_LESSONS;
import static com.leverx.learningmanagementsystem.utils.ITUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.utils.ITUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.utils.ITUtils.INSERT_SQL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LessonControllerIT extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllLessonsAnd200() throws Exception {
        // given
        var request = buildGetAllRequest(LESSONS, DEFAULT_PAGE, String.valueOf(TOTAL_NUMBER_OF_LESSONS));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_LESSONS));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_givenLessonId_shouldReturnLessonAnd200() throws Exception {
        // given
        var request = buildGetByIdRequest(LESSONS, String.valueOf(EXISTING_LESSON_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.id").value(EXISTING_LESSON_ID.toString()))
                .andExpect(jsonPath("$.title").value(EXISTING_LESSON_TITLE));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenCreateLessonDto_shouldReturnCreatedLessonAnd201() throws Exception {
        // given
        CreateLessonDto newLesson = LessonITUtils.initializeCreateLessonDto();
        var request = buildCreateRequest(LESSONS, APPLICATION_JSON, newLesson);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.title").value(NEW_LESSON_TITLE))
                .andExpect(jsonPath("$.courseId").value(NEW_LESSON_COURSE_ID.toString()));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void updateById_givenLessonIdAndCreateLessonDto_shouldReturnUpdatedLessonAnd200() throws Exception {
        // given
        CreateLessonDto newLesson = LessonITUtils.initializeCreateLessonDto();
        var request = buildUpdateByIdRequest(LESSONS, String.valueOf(EXISTING_LESSON_ID), APPLICATION_JSON, newLesson);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.title").value(NEW_LESSON_TITLE))
                .andExpect(jsonPath("$.courseId").value(NEW_LESSON_COURSE_ID.toString()));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnStatus204() throws Exception {
        // given
        var request = buildDeleteByIdRequest(LESSONS, String.valueOf(EXISTING_LESSON_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnNotFound() throws Exception {
        // given
        var request = buildDeleteByIdRequest(LESSONS, String.valueOf(NON_EXISTING_LESSON_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound());
    }
}
