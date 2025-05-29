package com.leverx.learningmanagementsystem.utils.scheduling;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.email.service.MustacheService;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.email.service.EmailService;
import com.leverx.learningmanagementsystem.utils.language.Language;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class SendCourseNotificationJob {

    private final CourseService courseService;
    private final EmailService emailService;
    private final MustacheService mustacheService;

    private final String TEMPLATE_PATH = "templates/email/course_notification_%s.mustache";
    private final String STUDENT_NAME = "student_name";
    private final String COURSE_TITLE = "course_title";
    private final String START_DATE = "start_date";

    @Scheduled(cron = "*/10 * * * * *")
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
        course.getStudents()
                .stream()
                .forEach(student -> {

                    String body = configureEmailBody(
                            student.getFirstName(),
                            student.getLanguage(),
                            course.getTitle(),
                            course.getSettings().getStartDate());

                    tryToSendCourseNotification(student.getEmail(), course.getTitle(), body);
                });
    }

    private String configureEmailBody(String studentName, Language language, String courseTitle, LocalDateTime startDate) {
        Map<String, Object> model = new HashMap<>();
        model.put(STUDENT_NAME, studentName);
        model.put(COURSE_TITLE, courseTitle);
        model.put(START_DATE, startDate);

        String templatePath = TEMPLATE_PATH.formatted(language.name().toLowerCase());
        return mustacheService.processTemplate(templatePath, model);
    }

    private void tryToSendCourseNotification(String email, String subject, String body) {
        try {
            emailService.sendEmail(email, subject, body);
        } catch (MessagingException | MailException ex) {
            log.error(ex.getMessage());
        }
    }
}
