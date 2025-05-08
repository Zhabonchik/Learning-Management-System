package com.leverx.learningmanagementsystem.student.mapper;

import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.dto.StudentResponseDto;
import com.leverx.learningmanagementsystem.student.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "courseIds", expression = "java(student.getCourses().stream()"
            + ".map(com.leverx.learningmanagementsystem.course.model.Course::getId)"
            + ".collect(java.util.stream.Collectors.toList()))")
    StudentResponseDto toDto(Student student);

    @Mapping(target = "courses", ignore = true)
    Student toModel(StudentResponseDto studentResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Student toModel(CreateStudentDto createStudentDto);

    List<StudentResponseDto> toDtos(List<Student> students);
}
