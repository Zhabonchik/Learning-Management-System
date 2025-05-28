package com.leverx.learningmanagementsystem.integration.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.config.security.SecurityEntityConfiguration;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import org.junit.jupiter.api.Tag;
import testutils.CourseTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static testutils.CourseTestUtils.COURSES;
import static testutils.CourseTestUtils.EXISTING_COURSE_ID;
import static testutils.CourseTestUtils.EXISTING_COURSE_TITLE;
import static testutils.CourseTestUtils.NEW_COURSE_DESCRIPTION;
import static testutils.CourseTestUtils.NEW_COURSE_TITLE;
import static testutils.CourseTestUtils.NON_EXISTING_COURSE_ID;
import static testutils.CourseTestUtils.NUMBER_OF_COURSE_FIELDS;
import static testutils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static testutils.TestUtils.CLEAN_SQL;
import static testutils.TestUtils.DEFAULT_PAGE;
import static testutils.TestUtils.INSERT_SQL;
import static testutils.TestUtils.PAGE;
import static testutils.TestUtils.PAGE_SIZE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Tag("integration-test")
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SecurityEntityConfiguration configuration;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllCourses() throws Exception {
        // when
        var response = mockMvc.perform(get(COURSES)
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_COURSES)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_COURSES));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnCourseAnd200() throws Exception {
        //when
        var response = mockMvc.perform(get(COURSES + "/" + EXISTING_COURSE_ID));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_FIELDS))
                .andExpect(jsonPath("$.title").value(EXISTING_COURSE_TITLE));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        // when
        var response = mockMvc.perform(get(COURSES + "/" + NON_EXISTING_COURSE_ID));

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Course not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenCreateCourseDto_shouldReturnCreatedCourseAnd201() throws Exception {
        // given
        CreateCourseDto newCourse = CourseTestUtils.initializeCreateCourseDto();

        // when
        var response = mockMvc.perform(post(COURSES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourse)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(NEW_COURSE_TITLE))
                .andExpect(jsonPath("$.description").value(NEW_COURSE_DESCRIPTION));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturn204() throws Exception {
        // when
        var response = mockMvc.perform(delete(COURSES + "/" + EXISTING_COURSE_ID));

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturn404() throws Exception {
        // when
        var response = mockMvc.perform(delete(COURSES + "/" + NON_EXISTING_COURSE_ID));

        // then
        response.andExpect(status().isNotFound());
    }
}
