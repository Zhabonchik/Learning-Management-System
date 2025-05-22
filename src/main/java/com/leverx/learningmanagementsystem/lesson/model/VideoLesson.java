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
@DiscriminatorValue("VIDEO")
public class VideoLesson extends Lesson {
    @Column(name = "url")
    private String url;

    @Column(name = "platform")
    private String platform;
}
