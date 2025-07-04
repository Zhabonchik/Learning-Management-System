package com.leverx.learningmanagementsystem.coursesettings.validator;

import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.core.exception.model.InvalidCourseDatesException;
import org.springframework.stereotype.Component;

import static com.leverx.learningmanagementsystem.core.constants.DateFormatConstants.DATE_TIME_FORMAT;
import static java.util.Objects.isNull;

@Component
public class CourseSettingsValidator {

    public void validateCourseDates(CourseSettings courseSettings) {
        if (isNull(courseSettings.getStartDate()) || isNull(courseSettings.getEndDate())) {
            throw new InvalidCourseDatesException(
                    "Invalid date format, expected format: " + DATE_TIME_FORMAT);
        }

        if (courseSettings.getStartDate().isAfter(courseSettings.getEndDate())) {
            throw new InvalidCourseDatesException(
                    "Course start date is after course end date");
        }
    }
}
