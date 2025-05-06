package com.leverx.learningmanagementsystem.utils;

import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.exception.InvalidCourseDatesException;

import static com.leverx.learningmanagementsystem.utils.DataFormatUtils.DATE_TIME_FORMAT;

public class CourseSettingsValidator {

    public static void validateCourseDates(CourseSettings courseSettings) {
        if (courseSettings.getStartDate() == null || courseSettings.getEndDate() == null) {
            throw new InvalidCourseDatesException(
                    "Invalid date format, expected format: " + DATE_TIME_FORMAT);
        }

        if (courseSettings.getStartDate().isAfter(courseSettings.getEndDate())) {
            throw new InvalidCourseDatesException(
                    "Course start date is after course end date");
        }
    }

}
