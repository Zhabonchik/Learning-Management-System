package com.leverx.learningmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "course")
@ToString(exclude = "course")
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private Integer durationInMinutes;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
