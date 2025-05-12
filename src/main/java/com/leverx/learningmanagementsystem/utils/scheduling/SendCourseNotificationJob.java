package com.leverx.learningmanagementsystem.utils.scheduling;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.email.service.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class SendCourseNotificationJob {

    private final CourseService courseService;
    private final EmailService emailService;

    @Scheduled(cron = "*/5 * * * * *")
    public void execute() {
        log.info("Fetching courses that start tomorrow");
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
        log.info("Trying to send notification");
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
