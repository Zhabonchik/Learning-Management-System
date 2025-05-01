package com.leverx.learningmanagementsystem.mapper.student;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.dto.student.GetStudentDto;
import com.leverx.learningmanagementsystem.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "courseIds", expression = "java(student.getCourses().stream()"
            + ".map(com.leverx.learningmanagementsystem.entity.Course::getId)"
            + ".collect(java.util.stream.Collectors.toList()))")
    GetStudentDto toGetStudentDto(Student student);

    @Mapping(target = "courses", ignore = true)
    Student toStudent(GetStudentDto getStudentDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Student toStudent(CreateStudentDto createStudentDto);

    List<GetStudentDto> toGetStudentDtoList(List<Student> students);
}
