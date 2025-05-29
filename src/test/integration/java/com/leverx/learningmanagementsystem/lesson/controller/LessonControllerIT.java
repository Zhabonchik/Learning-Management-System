package com.leverx.learningmanagementsystem.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
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
import static com.leverx.learningmanagementsystem.utils.ITUtils.PAGE;
import static com.leverx.learningmanagementsystem.utils.ITUtils.PAGE_SIZE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Tag("integration-test")
public class LessonControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllLessonsAnd200() throws Exception {
        // when
        var response = mockMvc.perform(get(LESSONS)
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_LESSONS)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_LESSONS));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_givenLessonId_shouldReturnLessonAnd200() throws Exception {
        // when
        var response = mockMvc.perform(get(LESSONS + "/" + EXISTING_LESSON_ID));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.id").value(EXISTING_LESSON_ID.toString()))
                .andExpect(jsonPath("$.title").value(EXISTING_LESSON_TITLE));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenCreateLessonDto_shouldReturnCreatedLessonAnd201() throws Exception {
        // given
        CreateLessonDto newLesson = LessonITUtils.initializeCreateLessonDto();

        // when
        var response = mockMvc.perform(post(LESSONS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLesson)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.title").value(NEW_LESSON_TITLE))
                .andExpect(jsonPath("$.courseId").value(NEW_LESSON_COURSE_ID.toString()));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void updateById_givenLessonIdAndCreateLessonDto_shouldReturnUpdatedLessonAnd200() throws Exception {
        // given
        CreateLessonDto newLesson = LessonITUtils.initializeCreateLessonDto();

        // when
        var response = mockMvc.perform(put(LESSONS + "/" + EXISTING_LESSON_ID)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLesson)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_LESSON_FIELDS))
                .andExpect(jsonPath("$.title").value(NEW_LESSON_TITLE))
                .andExpect(jsonPath("$.courseId").value(NEW_LESSON_COURSE_ID.toString()));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnStatus204() throws Exception {
        // when
        var response = mockMvc.perform(delete(LESSONS + "/" + EXISTING_LESSON_ID));

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnNotFound() throws Exception {
        // when
        var response = mockMvc.perform(delete(LESSONS + "/" + NON_EXISTING_LESSON_ID));

        // then
        response.andExpect(status().isNotFound());
    }
}
