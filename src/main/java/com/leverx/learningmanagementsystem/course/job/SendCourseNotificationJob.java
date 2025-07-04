package com.leverx.learningmanagementsystem.course.job;

import com.leverx.learningmanagementsystem.course.job.service.CourseNotificationSender;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.email.service.impl.EmailTemplateBuilder;
import com.leverx.learningmanagementsystem.student.model.Student;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class SendCourseNotificationJob {

    public static final String STUDENT_NAME = "student_name";
    public static final String COURSE_TITLE = "course_title";
    public static final String START_DATE = "start_date";

    private final CourseService courseService;
    private final CourseNotificationSender courseNotificationSender;
    private final EmailTemplateBuilder emailTemplateBuilder;

    @Scheduled(cron = "0 */1 * * * *")
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
                .forEach(student ->
                    sendNotification(student, course)
                );
    }

    private void sendNotification(Student student, Course course) {
        String body = configureEmailBody(
                student.getFirstName(),
                student.getLocale(),
                course.getTitle(),
                course.getSettings().getStartDate());

        courseNotificationSender.send(student.getEmail(), course.getTitle(), body);
    }

    private String configureEmailBody(String studentName, Locale locale, String courseTitle, LocalDateTime startDate) {
        Map<String, String> model = new HashMap<>();
        model.put(STUDENT_NAME, studentName);
        model.put(COURSE_TITLE, courseTitle);
        model.put(START_DATE, startDate.toString());

        LocaleContextHolder.setLocale(locale);
        return emailTemplateBuilder.buildForCourseStarting(model);
    }
}
