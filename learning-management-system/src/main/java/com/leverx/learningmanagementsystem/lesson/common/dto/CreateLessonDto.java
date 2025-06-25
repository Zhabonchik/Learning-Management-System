package com.leverx.learningmanagementsystem.lesson.common.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.leverx.learningmanagementsystem.lesson.classroom.dto.CreateClassroomLessonDto;
import com.leverx.learningmanagementsystem.lesson.video.dto.CreateVideoLessonDto;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateVideoLessonDto.class, name = "VIDEO"),
        @JsonSubTypes.Type(value = CreateClassroomLessonDto.class, name = "CLASSROOM")
})
public interface CreateLessonDto {

    UUID courseId();

    String title();

    Integer durationInMinutes();
}
