package com.leverx.learningmanagementsystem.testutils;

import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CourseSettingsTestUtils {

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
    public static final int NUMBER_OF_COURSE_SETTINGS_FIELDS = 8;
    public static final int TOTAL_NUMBER_OF_COURSE_SETTINGS = 2;
    public static final String COURSE_SETTINGS = "/course-settings";

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CreateCourseSettingsDto initializeCreateCourseSettingsDto() {
        return new CreateCourseSettingsDto(
                NEW_COURSE_SETTINGS_START_DATE,
                NEW_COURSE_SETTINGS_END_DATE,
                NEW_COURSE_SETTINGS_IS_PUBLIC
        );
    }

    private CourseSettingsTestUtils() {

    }
}
