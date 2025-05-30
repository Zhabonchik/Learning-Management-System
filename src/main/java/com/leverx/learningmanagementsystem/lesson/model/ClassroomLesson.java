package com.leverx.learningmanagementsystem.lesson.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "classroom_lesson")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClassroomLesson extends Lesson {

    @Column(name = "location")
    private String location;

    @Column(name = "capacity")
    private Integer capacity;
}
