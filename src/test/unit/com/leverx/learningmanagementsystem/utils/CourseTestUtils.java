package com.leverx.learningmanagementsystem.utils;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.student.model.Student;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CourseTestUtils {

    public static final String NEW_COURSE_TITLE = "Test course";
    public static final String NEW_COURSE_DESCRIPTION = "This is a test course";
    public static final BigDecimal NEW_COURSE_PRICE = BigDecimal.valueOf(143);
    public static final BigDecimal NEW_COURSE_COINS_PAID = BigDecimal.valueOf(1430);
    public static final UUID NEW_COURSE_SETTINGS_ID = null;
    public static final CourseSettings NEW_COURSE_SETTINGS = null;
    public static final List<UUID> NEW_COURSE_LESSON_IDS = new ArrayList<>();
    public static final List<Lesson> NEW_COURSE_LESSONS = new ArrayList<>();
    public static final List<UUID> NEW_COURSE_STUDENT_IDS = new ArrayList<>();
    public static final List<Student> NEW_COURSE_STUDENTS = new ArrayList<>();
    public static final UUID EXISTING_COURSE_ID = UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0");
    public static final String EXISTING_COURSE_TITLE = "Applied Math";
    public static final Instant EXISTING_COURSE_CREATED = Instant.now();
    public static final Instant EXISTING_COURSE_LAST_MODIFIED = Instant.now();
    public static final String EXISTING_COURSE_CREATED_BY = "Anonymous user";
    public static final String EXISTING_COURSE_LAST_MODIFIED_BY = "Anonymous user";
    public static final UUID NON_EXISTING_COURSE_ID = UUID.fromString("2bcd9463-3c57-421b-91d0-047b315d60ce");
    public static final int TOTAL_NUMBER_OF_COURSES = 2;
    public static final int NUMBER_OF_COURSE_FIELDS = 12;
    public static final String COURSES = "/courses";

    public static CreateCourseDto initializeCreateCourseDto() {
        return new CreateCourseDto(
                NEW_COURSE_TITLE,
                NEW_COURSE_DESCRIPTION,
                NEW_COURSE_PRICE,
                NEW_COURSE_COINS_PAID,
                NEW_COURSE_SETTINGS_ID,
                NEW_COURSE_LESSON_IDS,
                NEW_COURSE_STUDENT_IDS
        );
    }

    public static CourseResponseDto initializeCourseResponseDto() {
        return new CourseResponseDto(
                EXISTING_COURSE_ID,
                EXISTING_COURSE_TITLE,
                NEW_COURSE_DESCRIPTION,
                NEW_COURSE_PRICE,
                NEW_COURSE_COINS_PAID,
                NEW_COURSE_SETTINGS_ID,
                EXISTING_COURSE_CREATED,
                EXISTING_COURSE_CREATED_BY,
                EXISTING_COURSE_LAST_MODIFIED,
                EXISTING_COURSE_LAST_MODIFIED_BY,
                NEW_COURSE_LESSON_IDS,
                NEW_COURSE_STUDENT_IDS
        );
    }

    public static Course initializeCourse() {
        return Course.builder()
                .title(NEW_COURSE_TITLE)
                .description(NEW_COURSE_DESCRIPTION)
                .price(NEW_COURSE_PRICE)
                .coinsPaid(NEW_COURSE_COINS_PAID)
                .settings(NEW_COURSE_SETTINGS)
                .lessons(NEW_COURSE_LESSONS)
                .students(NEW_COURSE_STUDENTS)
                .build();
    }

    public static Page<CourseResponseDto> initializePage(Pageable pageable) {
        CourseResponseDto firstResponseDto = initializeCourseResponseDto();
        CourseResponseDto secondResponseDto = initializeCourseResponseDto();
        List<CourseResponseDto> response = List.of(firstResponseDto, secondResponseDto);
        return new PageImpl<>(response, pageable, response.size());
    }
}
