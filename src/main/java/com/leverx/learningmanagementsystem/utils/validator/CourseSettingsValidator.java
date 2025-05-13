package com.leverx.learningmanagementsystem.utils.validator;

import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.utils.exception.model.InvalidCourseDatesException;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.leverx.learningmanagementsystem.utils.DataFormatUtils.DATE_TIME_FORMAT;

@Component
public class CourseSettingsValidator {

    public void validateCourseDates(CourseSettings courseSettings) {
        if (Objects.isNull(courseSettings.getStartDate()) || Objects.isNull(courseSettings.getEndDate())) {
            throw new InvalidCourseDatesException(
                    "Invalid date format, expected format: " + DATE_TIME_FORMAT);
        }

        if (courseSettings.getStartDate().isAfter(courseSettings.getEndDate())) {
            throw new InvalidCourseDatesException(
                    "Course start date is after course end date");
        }
    }

}
