package com.leverx.learningmanagementsystem.lesson.mapper;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.dto.ClassroomLesson.ClassroomLessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.dto.ClassroomLesson.CreateClassroomLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.dto.VideoLesson.CreateVideoLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.VideoLesson.VideoLessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.model.ClassroomLesson;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.lesson.model.VideoLesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    default LessonResponseDto toDto(Lesson lesson) {
        if (lesson instanceof VideoLesson videoLesson) {
            return toDto(videoLesson);
        } else if (lesson instanceof ClassroomLesson classroomLesson) {
            return toDto(classroomLesson);
        }
        throw new IllegalArgumentException("Unknown lesson type: " + lesson.getClass());
    }

    default Lesson toModel(CreateLessonDto dto) {
        if (dto instanceof CreateVideoLessonDto videoDto) {
            return toModel(videoDto);
        } else if (dto instanceof CreateClassroomLessonDto classroomDto) {
            return toModel(classroomDto);
        }
        throw new IllegalArgumentException("Unknown createLessonDto type: " + dto.getClass());
    }

    default Lesson toModel(LessonResponseDto dto) {
        if (dto instanceof VideoLessonResponseDto videoDto) {
            return toModel(videoDto);
        } else if (dto instanceof ClassroomLessonResponseDto classroomDto) {
            return toModel(classroomDto);
        }
        throw new IllegalArgumentException("Unknown lessonResponseDto type: " + dto.getClass());
    }

    @Mapping(target = "courseId", source = "course", qualifiedByName = "mapToCourseId")
    VideoLessonResponseDto toDto(VideoLesson lesson);

    @Mapping(target = "courseId", source = "course", qualifiedByName = "mapToCourseId")
    ClassroomLessonResponseDto toDto(ClassroomLesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    VideoLesson toModel(CreateVideoLessonDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    ClassroomLesson toModel(CreateClassroomLessonDto dto);

    default List<LessonResponseDto> toDtos(List<Lesson> lessons) {
        return lessons.stream()
                .map(this::toDto)
                .toList();
    }

    @Named("mapToCourseId")
    default UUID mapToCourseId(Course course) {
        return (isNull(course)) ? null : course.getId();
    }
}
