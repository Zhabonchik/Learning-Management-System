package com.leverx.learningmanagementsystem.testutils;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.model.Student;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentTestUtils {

    public static final String NEW_STUDENT_FIRST_NAME = "A";
    public static final String NEW_STUDENT_LAST_NAME = "B";
    public static final String NEW_STUDENT_EMAIL = "email@gmail.com";
    public static final LocalDate NEW_STUDENT_DATE_OF_BIRTH = LocalDate.of(2005, 7, 23);
    public static final BigDecimal NEW_STUDENT_COINS = new BigDecimal(1548);
    public static final List<Course> NEW_STUDENT_COURSES = new ArrayList<>();
    public static final List<UUID> NEW_STUDENT_COURSE_IDS = new ArrayList<>();
    public static final UUID NON_EXISTING_STUDENT_ID = UUID.fromString("2bcd9463-3c57-421b-91d0-047b315d60ce");
    public static final UUID EXISTING_STUDENT_ID = UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1");
    public static final String EXISTING_STUDENT_FIRST_NAME = "Abap";
    public static final String EXISTING_STUDENT_LAST_NAME = "Abapov";
    public static final String STUDENTS = "/students";
    public static final int TOTAL_NUMBER_OF_STUDENTS = 2;
    public static final int NUMBER_OF_STUDENT_FIELDS = 7;

    public static CreateStudentDto initializeCreateStudentDto() {
        return new CreateStudentDto(
                NEW_STUDENT_FIRST_NAME,
                NEW_STUDENT_LAST_NAME,
                NEW_STUDENT_EMAIL,
                NEW_STUDENT_DATE_OF_BIRTH,
                NEW_STUDENT_COINS,
                NEW_STUDENT_COURSE_IDS
        );
    }

    public static Student initializeStudent() {
        return Student.builder()
                .firstName(NEW_STUDENT_FIRST_NAME)
                .lastName(NEW_STUDENT_LAST_NAME)
                .email(NEW_STUDENT_EMAIL)
                .dateOfBirth(NEW_STUDENT_DATE_OF_BIRTH)
                .coins(NEW_STUDENT_COINS)
                .courses(NEW_STUDENT_COURSES)
                .build();
    }

    private StudentTestUtils() {

    }
}
