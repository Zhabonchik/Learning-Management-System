package com.leverx.learningmanagementsystem.scheduling;

import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.service.CourseService;
import com.leverx.learningmanagementsystem.service.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class SendCourseNotificationJob {

    private final CourseService courseService;
    private final EmailService emailService;

    @Autowired
    public SendCourseNotificationJob(CourseService courseService, EmailService emailService) {
        this.courseService = courseService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "@daily")
    public void execute() {
        List<Course> courses = getCoursesStartingTomorrow();
        courses.forEach(this::prepareAndSendNotification);
    }

    private List<Course> getCoursesStartingTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime tomorrowStart = tomorrow.atStartOfDay();
        LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1).minusSeconds(1);

        return courseService.getAllStartingBetweenDates(tomorrowStart, tomorrowEnd);
    }

    private void prepareAndSendNotification(Course course) {
        List<String> emails = course.getStudents().stream().map(Student::getEmail).toList();
        String subject = course.getTitle();
        String body = "Dear student, course %s is starting soon. The exact date is %s."
                .formatted(course.getTitle(), course.getSettings().getStartDate());

        tryToSendCourseNotification(emails, subject, body);
    }

    private void tryToSendCourseNotification(List<String> emails, String subject, String body) {
        try {
            emailService.sendEmail(emails, subject, body);
        } catch (MessagingException | MailException ex) {
            log.error(ex.getMessage());
        }
    }

}
