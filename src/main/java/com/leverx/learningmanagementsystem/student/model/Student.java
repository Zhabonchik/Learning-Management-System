package com.leverx.learningmanagementsystem.student.model;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.utils.language.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = "courses")
@ToString(exclude = "courses")
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "coins")
    private BigDecimal coins;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

}
