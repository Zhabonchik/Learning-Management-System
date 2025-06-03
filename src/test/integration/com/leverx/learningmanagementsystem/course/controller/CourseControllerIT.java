package com.leverx.learningmanagementsystem.course.controller;

import com.leverx.learningmanagementsystem.AbstractCommonIT;
import com.leverx.learningmanagementsystem.course.builder.CourseRequestBuilder;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.COURSES;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.EXISTING_COURSE_TITLE;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.NEW_COURSE_DESCRIPTION;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.NEW_COURSE_TITLE;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.NON_EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.NUMBER_OF_COURSE_FIELDS;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.TOTAL_NUMBER_OF_COURSES;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseITUtils.initializeCreateCourseDto;
import static com.leverx.learningmanagementsystem.common.utils.ITUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.common.utils.ITUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.common.utils.ITUtils.INSERT_SQL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CourseControllerIT extends AbstractCommonIT {

    @Autowired
    CourseRequestBuilder requestBuilder;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllCourses() throws Exception {
        // given
        var request = requestBuilder.buildGetAllRequest(COURSES, DEFAULT_PAGE, String.valueOf(TOTAL_NUMBER_OF_COURSES));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.courseResponseDtoList.size()").value(TOTAL_NUMBER_OF_COURSES));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnCourseAnd200() throws Exception {
        // given
        var request = requestBuilder.buildGetByIdRequest(COURSES, String.valueOf(EXISTING_COURSE_ID));

        //when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_FIELDS))
                .andExpect(jsonPath("$.title").value(EXISTING_COURSE_TITLE));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        // given
        var request = requestBuilder.buildGetByIdRequest(COURSES, String.valueOf(NON_EXISTING_COURSE_ID));

        //when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Course not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCreateCourseDto_shouldReturnCreatedCourseAnd201() throws Exception {
        // given
        CreateCourseDto newCourse = initializeCreateCourseDto();
        var request = requestBuilder.buildCreateRequest(COURSES, APPLICATION_JSON, newCourse);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(NEW_COURSE_TITLE))
                .andExpect(jsonPath("$.description").value(NEW_COURSE_DESCRIPTION));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturn204() throws Exception {
        // given
        var request = requestBuilder.buildDeleteByIdRequest(COURSES, String.valueOf(EXISTING_COURSE_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturn404() throws Exception {
        // given
        var request = requestBuilder.buildDeleteByIdRequest(COURSES, String.valueOf(NON_EXISTING_COURSE_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound());
    }
}
