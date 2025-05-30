package com.leverx.learningmanagementsystem.course.model;

import com.leverx.learningmanagementsystem.core.audit.model.AuditableEntity;
import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.student.model.Student;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"settings", "lessons", "students"}, callSuper = false)
@ToString(exclude = {"settings", "lessons", "students"})
@Builder
public class Course extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "coins_paid")
    private BigDecimal coinsPaid;

    @OneToOne(cascade = {MERGE, REFRESH})
    @JoinColumn(name = "settings_id", referencedColumnName = "id")
    private CourseSettings settings;

    @OneToMany(mappedBy = "course", cascade = {MERGE, REFRESH})
    private List<Lesson> lessons;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
}
