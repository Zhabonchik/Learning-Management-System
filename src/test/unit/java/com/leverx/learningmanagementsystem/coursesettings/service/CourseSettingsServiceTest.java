package com.leverx.learningmanagementsystem.coursesettings.service;

import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.coursesettings.repository.CourseSettingsRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.leverx.learningmanagementsystem.utils.CourseSettingsTestUtils.EXISTING_COURSE_SETTINGS_ID;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsTestUtils.TOTAL_NUMBER_OF_COURSE_SETTINGS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit-test")
public class CourseSettingsServiceTest {

    @Mock
    CourseSettingsRepository courseSettingsRepository;

    @InjectMocks
    CourseSettingsServiceImpl courseSettingsService;

    @Test
    void getAll_shouldReturnAllCourseSettings() {
        // given
        CourseSettings firstCourseSettings = new CourseSettings();
        CourseSettings secondCourseSettings = new CourseSettings();
        firstCourseSettings.setId(EXISTING_COURSE_SETTINGS_ID);
        secondCourseSettings.setId(EXISTING_COURSE_SETTINGS_ID);
        when(courseSettingsRepository.findAll()).thenReturn(List.of(firstCourseSettings, secondCourseSettings));

        // when
        var courseSettings = courseSettingsService.getAll();

        // then
        assertAll(
                () -> assertEquals(TOTAL_NUMBER_OF_COURSE_SETTINGS, courseSettings.size()),
                () -> assertEquals(EXISTING_COURSE_SETTINGS_ID, courseSettings.get(0).getId()),
                () -> assertEquals(EXISTING_COURSE_SETTINGS_ID, courseSettings.get(1).getId())
        );
    }

    @Test
    void getById_givenId_shouldReturnCourseSettings() {
        // given
        CourseSettings existingCourseSettings = new CourseSettings();
        existingCourseSettings.setId(EXISTING_COURSE_SETTINGS_ID);
        when(courseSettingsRepository.findById(EXISTING_COURSE_SETTINGS_ID)).thenReturn(Optional.of(existingCourseSettings));

        // when
        var courseSettings = courseSettingsService.getById(EXISTING_COURSE_SETTINGS_ID);

        // then
        assertEquals(EXISTING_COURSE_SETTINGS_ID, courseSettings.getId());
    }

    @Test
    void create_givenCourseSettings_shouldReturnCourseSettings() {
        // given
        CourseSettings newCourseSettings = new CourseSettings();
        newCourseSettings.setId(EXISTING_COURSE_SETTINGS_ID);
        when(courseSettingsRepository.save(any(CourseSettings.class))).thenReturn(newCourseSettings);

        // when
        var course = courseSettingsRepository.save(newCourseSettings);

        // then
        assertEquals(EXISTING_COURSE_SETTINGS_ID, course.getId());
    }
}
