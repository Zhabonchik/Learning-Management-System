package com.leverx.learningmanagementsystem.coursesettings.common.utils;

import com.leverx.learningmanagementsystem.coursesettings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CourseSettingsITUtils {

    public static final LocalDateTime NEW_COURSE_SETTINGS_START_DATE =
            LocalDateTime.of(2026,5, 6, 10,0,0);
    public static final LocalDateTime NEW_COURSE_SETTINGS_END_DATE =
            LocalDateTime.of(2026,6, 1, 10,0,0);
    public static final boolean NEW_COURSE_SETTINGS_IS_PUBLIC = true;
    public static final UUID NON_EXISTING_COURSE_SETTINGS_ID = UUID.fromString("11111913-ce40-4ef4-a596-f24d94356111");
    public static final UUID EXISTING_COURSE_SETTINGS_ID = UUID.fromString("38811913-ce40-4ef4-a596-f24d94356949");
    public static final LocalDateTime EXISTING_COURSE_SETTINGS_START_DATE =
            LocalDateTime.of(2025, 5,13, 9, 30, 0);
    public static final LocalDateTime EXISTING_COURSE_SETTINGS_END_DATE =
            LocalDateTime.of(2025, 6,23, 15,0,0);
    public static final Instant EXISTING_COURSE_SETTINGS_CREATED = Instant.now();
    public static final Instant EXISTING_COURSE_SETTINGS_LAST_MODIFIED = Instant.now();
    public static final String EXISTING_COURSE_SETTINGS_CREATED_BY = "Anonymous user";
    public static final String EXISTING_COURSE_SETTINGS_LAST_MODIFIED_BY = "Anonymous user";
    public static final int NUMBER_OF_COURSE_SETTINGS_FIELDS = 8;
    public static final int TOTAL_NUMBER_OF_COURSE_SETTINGS = 2;
    public static final String COURSE_SETTINGS = "/course-settings";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CreateCourseSettingsDto initializeCreateCourseSettingsDto() {
        return new CreateCourseSettingsDto(
                NEW_COURSE_SETTINGS_START_DATE,
                NEW_COURSE_SETTINGS_END_DATE,
                NEW_COURSE_SETTINGS_IS_PUBLIC
        );
    }

    public static CourseSettingsResponseDto initializeCourseSettingsResponseDto() {
        return new CourseSettingsResponseDto(
                EXISTING_COURSE_SETTINGS_ID,
                EXISTING_COURSE_SETTINGS_START_DATE,
                EXISTING_COURSE_SETTINGS_END_DATE,
                NEW_COURSE_SETTINGS_IS_PUBLIC,
                EXISTING_COURSE_SETTINGS_CREATED,
                EXISTING_COURSE_SETTINGS_CREATED_BY,
                EXISTING_COURSE_SETTINGS_LAST_MODIFIED,
                EXISTING_COURSE_SETTINGS_LAST_MODIFIED_BY
        );
    }

    public static Page<CourseSettingsResponseDto> initializePage(Pageable pageable) {
        CourseSettingsResponseDto firstDto = initializeCourseSettingsResponseDto();
        CourseSettingsResponseDto secondDto = initializeCourseSettingsResponseDto();
        List<CourseSettingsResponseDto> responseDtos = List.of(firstDto, secondDto);
        return new PageImpl<>(responseDtos, pageable, responseDtos.size());
    }
}
