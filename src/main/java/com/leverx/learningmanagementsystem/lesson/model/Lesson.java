package com.leverx.learningmanagementsystem.lesson.model;

import com.leverx.learningmanagementsystem.course.model.Course;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "course")
@ToString(exclude = "course")
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Lesson {

    @Id
    @GeneratedValue(strategy = UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private Integer durationInMinutes;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
