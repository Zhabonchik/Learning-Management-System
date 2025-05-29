package com.leverx.learningmanagementsystem.lesson.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "video_lesson")
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoLesson extends Lesson {
    @Column(name = "url")
    private String url;

    @Column(name = "platform")
    private String platform;
}
