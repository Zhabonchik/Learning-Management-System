package com.leverx.learningmanagementsystem.lesson.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("CLASSROOM")
public class ClassroomLesson extends Lesson {

    @Column(name = "location")
    private String location;

    @Column(name = "capacity")
    private Integer capacity;
}
