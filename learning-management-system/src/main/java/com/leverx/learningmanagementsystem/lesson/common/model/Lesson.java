package com.leverx.learningmanagementsystem.lesson.common.model;

import com.leverx.learningmanagementsystem.core.audit.model.AuditableEntity;
import com.leverx.learningmanagementsystem.course.model.Course;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Table(name = "lesson")
@Data
@ToString(exclude = "course")
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = JOINED)
public abstract class Lesson extends AuditableEntity {

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
