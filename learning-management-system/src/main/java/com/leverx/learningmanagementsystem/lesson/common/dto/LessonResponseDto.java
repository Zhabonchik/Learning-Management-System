package com.leverx.learningmanagementsystem.lesson.common.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.leverx.learningmanagementsystem.lesson.classroom.dto.ClassroomLessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.video.dto.VideoLessonResponseDto;

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
