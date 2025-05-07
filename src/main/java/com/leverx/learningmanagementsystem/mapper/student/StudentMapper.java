package com.leverx.learningmanagementsystem.mapper.student;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.StudentResponseDto;
import com.leverx.learningmanagementsystem.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "courseIds", expression = "java(student.getCourses().stream()"
            + ".map(com.leverx.learningmanagementsystem.entity.Course::getId)"
            + ".collect(java.util.stream.Collectors.toList()))")
    StudentResponseDto toDto(Student student);

    @Mapping(target = "courses", ignore = true)
    Student toModel(StudentResponseDto studentResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Student toModel(CreateStudentDto createStudentDto);

    List<StudentResponseDto> toDtos(List<Student> students);
}
