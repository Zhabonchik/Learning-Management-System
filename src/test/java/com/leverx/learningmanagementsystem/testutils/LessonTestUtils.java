package com.leverx.learningmanagementsystem.testutils;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonType;
import com.leverx.learningmanagementsystem.lesson.dto.VideoLesson.CreateVideoLessonDto;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.lesson.model.VideoLesson;

import java.util.UUID;

import static com.leverx.learningmanagementsystem.lesson.dto.LessonType.VIDEO;

public class LessonTestUtils {

    public static final String NEW_LESSON_TITLE = "New Lesson";
    public static final Integer NEW_LESSON_DURATION_IN_MINUTES = 80;
    public static final Course NEW_LESSON_COURSE = null;
    public static final UUID NEW_LESSON_COURSE_ID = UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0");
    public static final UUID NON_EXISTING_LESSON_ID = UUID.fromString("891eed08-bc3a-4f57-85f5-bd1fb8b6eed6");
    public static final String NEW_VIDEO_LESSON_URL = "url";
    public static final String NEW_VIDEO_LESSON_PLATFORM = "platform";
    public static final UUID EXISTING_LESSON_ID = UUID.fromString("373e60bb-c872-4207-982c-859b4dfdb4f7");
    public static final String EXISTING_LESSON_TITLE = "Linear algebra";
    public static final int TOTAL_NUMBER_OF_LESSONS = 2;
    public static final int NUMBER_OF_LESSON_FIELDS = 11;
    public static final String LESSONS = "/lessons";

    public static CreateLessonDto initializeCreateLessonDto() {
        return new CreateVideoLessonDto(
                NEW_LESSON_TITLE,
                NEW_LESSON_DURATION_IN_MINUTES,
                NEW_LESSON_COURSE_ID,
                NEW_VIDEO_LESSON_URL,
                NEW_VIDEO_LESSON_PLATFORM
        );
    }

    public static Lesson initializeLesson() {
        Lesson lesson = new VideoLesson();
        lesson.setTitle(NEW_LESSON_TITLE);
        lesson.setDurationInMinutes(NEW_LESSON_DURATION_IN_MINUTES);
        lesson.setCourse(NEW_LESSON_COURSE);
        return lesson;
    }

    private LessonTestUtils() {
    }
}
