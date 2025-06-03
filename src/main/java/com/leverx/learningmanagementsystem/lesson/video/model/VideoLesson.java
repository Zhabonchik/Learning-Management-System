package com.leverx.learningmanagementsystem.lesson.video.model;

import com.leverx.learningmanagementsystem.lesson.common.model.Lesson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
