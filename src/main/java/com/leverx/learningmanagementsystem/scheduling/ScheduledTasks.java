package com.leverx.learningmanagementsystem.scheduling;

import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.service.CourseService;
import com.leverx.learningmanagementsystem.service.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    private final CourseService courseService;
    private final EmailService emailService;

    @Autowired
    public ScheduledTasks(CourseService courseService, EmailService emailService) {
        this.courseService = courseService;
        this.emailService = emailService;
    }

    @Scheduled(initialDelay = 10_000, fixedRate = 10_000)
    public void getCoursesStartingTomorrow() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime tomorrowStart = tomorrow.atStartOfDay();
        LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1).minusSeconds(1);

        List<Course> courses = courseService.getAllStartingBetweenDates(tomorrowStart, tomorrowEnd);

        for (Course course : courses) {
            List<String> emails = course.getStudents().stream().map(Student::getEmail).toList();
            String subject = course.getTitle();
            String body = "Dear student, course " + course.getTitle() + " is starting soon. The exact date is "
                    + course.getSettings().getStartDate();
            try{
                emailService.sendEmail(emails, subject, body);
            } catch (MessagingException ex) {
                log.error(ex.getMessage());
            }
        }

    }

}
