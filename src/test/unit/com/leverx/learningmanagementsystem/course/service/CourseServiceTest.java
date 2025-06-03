package com.leverx.learningmanagementsystem.course.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.course.repository.CourseRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.leverx.learningmanagementsystem.course.common.utils.CourseTestUtils.EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseTestUtils.NEW_COURSE_TITLE;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static com.leverx.learningmanagementsystem.course.common.utils.CourseTestUtils.initializeCourse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void getAll_ShouldReturnAllCourses() {
        // given
        Course firstCourse = initializeCourse();
        Course secondCourse = initializeCourse();
        when(courseRepository.findAllWithLessons()).thenReturn(List.of(firstCourse, secondCourse));
        when(courseRepository.findAllWithStudents()).thenReturn(List.of(firstCourse, secondCourse));
        when(courseRepository.findAllWithSettings()).thenReturn(List.of(firstCourse, secondCourse));

        // when
        var courses = courseService.getAll();

        //then
        assertAll(
                () -> assertEquals(TOTAL_NUMBER_OF_COURSES, courses.size()),
                () -> assertEquals(firstCourse.getId(), courses.get(0).getId()),
                () -> assertEquals(secondCourse.getId(), courses.get(1).getId())
        );
    }

    @Test
    void getById_givenId_ShouldReturnCourse() {
        // given
        Course existingCourse = initializeCourse();
        when(courseRepository.findById(EXISTING_COURSE_ID)).thenReturn(Optional.of(existingCourse));

        // when
        Course course = courseService.getById(EXISTING_COURSE_ID);

        // then
        assertEquals(NEW_COURSE_TITLE, course.getTitle());
    }

    @Test
    void create_givenCourse_ShouldReturnCourse() {
        // given
        Course newCourse = initializeCourse();
        when(courseRepository.save(any(Course.class))).thenReturn(newCourse);

        //when
        Course course = courseService.create(newCourse);

        //then
        assertEquals(NEW_COURSE_TITLE, course.getTitle());
    }

}
