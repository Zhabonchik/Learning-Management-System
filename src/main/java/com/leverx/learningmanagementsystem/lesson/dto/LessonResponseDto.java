package com.leverx.learningmanagementsystem.lesson.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.leverx.learningmanagementsystem.lesson.dto.ClassroomLesson.ClassroomLessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.dto.VideoLesson.VideoLessonResponseDto;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VideoLessonResponseDto.class, name = "VIDEO"),
        @JsonSubTypes.Type(value = ClassroomLessonResponseDto.class, name = "CLASSROOM")
})
public interface LessonResponseDto {
}
