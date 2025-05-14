package com.leverx.learningmanagementsystem.student.mapper;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentResponseDto;
import com.leverx.learningmanagementsystem.student.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "courseIds", source = "courses", qualifiedByName = "mapToCourseIds")
    StudentResponseDto toDto(Student student);

    @Mapping(target = "courses", ignore = true)
    Student toModel(StudentResponseDto studentResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Student toModel(CreateStudentDto createStudentDto);

    List<StudentResponseDto> toDtos(List<Student> students);

    @Named("mapToCourseIds")
    default List<UUID> mapToCourseIds(List<Course> courses) {
        return courses.stream()
                .map(Course::getId)
                .toList();
    }
}
