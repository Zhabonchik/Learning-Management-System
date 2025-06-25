package com.leverx.learningmanagementsystem.lesson.controller;

import com.leverx.learningmanagementsystem.AbstractCommonIT;
import com.leverx.learningmanagementsystem.lesson.builder.LessonRequestBuilder;
import com.leverx.learningmanagementsystem.lesson.common.dto.CreateLessonDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils;

import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.EXISTING_LESSON_TITLE;
import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.LESSONS;
import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.NEW_LESSON_COURSE_ID;
import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.NEW_LESSON_TITLE;
import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.NON_EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.NUMBER_OF_LESSON_FIELDS;
import static com.leverx.learningmanagementsystem.lesson.common.utils.LessonITUtils.TOTAL_NUMBER_OF_LESSONS;
import static com.leverx.learningmanagementsystem.common.utils.ITUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.common.utils.ITUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.common.utils.ITUtils.INSERT_SQL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LessonControllerIT extends AbstractCommonIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRequestBuilder requestBuilder;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllLessonsAnd200() throws Exception {
        // given
        var request = requestBuilder.buildGetAllRequest(LESSONS, DEFAULT_PAGE, String.valueOf(TOTAL_NUMBER_OF_LESSONS));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_LESSONS));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenLessonId_shouldReturnLessonAnd200() throws Exception {
        // given
        var request = requestBuilder.buildGetByIdRequest(LESSONS, String.valueOf(EXISTING_LESSON_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.id").value(EXISTING_LESSON_ID.toString()))
                .andExpect(jsonPath("$.title").value(EXISTING_LESSON_TITLE));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCreateLessonDto_shouldReturnCreatedLessonAnd201() throws Exception {
        // given
        CreateLessonDto newLesson = LessonITUtils.initializeCreateLessonDto();
        var request = requestBuilder.buildCreateRequest(LESSONS, APPLICATION_JSON, newLesson);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.title").value(NEW_LESSON_TITLE))
                .andExpect(jsonPath("$.courseId").value(NEW_LESSON_COURSE_ID.toString()));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateById_givenLessonIdAndCreateLessonDto_shouldReturnUpdatedLessonAnd200() throws Exception {
        // given
        CreateLessonDto newLesson = LessonITUtils.initializeCreateLessonDto();
        var request = requestBuilder.buildUpdateByIdRequest(LESSONS, String.valueOf(EXISTING_LESSON_ID), APPLICATION_JSON, newLesson);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.title").value(NEW_LESSON_TITLE))
                .andExpect(jsonPath("$.courseId").value(NEW_LESSON_COURSE_ID.toString()));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturnStatus204() throws Exception {
        // given
        var request = requestBuilder.buildDeleteByIdRequest(LESSONS, String.valueOf(EXISTING_LESSON_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturnNotFound() throws Exception {
        // given
        var request = requestBuilder.buildDeleteByIdRequest(LESSONS, String.valueOf(NON_EXISTING_LESSON_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound());
    }
}
