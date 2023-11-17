package features.weekly_schedule;

import java.io.IOException;
import java.util.ArrayList;

import core.models.concretes.Course;
import core.repositories.CourseRepository;

import core.general_providers.SessionController;
import core.general_providers.TerminalManager;
import features.main_menu.MenuController;
import core.models.concretes.Student;

public class WeeklyScheduleController {

    private WeeklyScheduleView weeklyScheduleView;
    private CourseRepository courseRepository;

    public WeeklyScheduleController() {
        this.weeklyScheduleView = new WeeklyScheduleView();
        this.courseRepository = new CourseRepository();
        handleWeeklySchedule();
    }

    private ArrayList<Course> fetchCourses() throws IOException {

        // UNCOMMENT:
        Student currentStudent = (Student) (SessionController.getInstance().getCurrentUser());
        int currentSemester = currentStudent.getTranscript().getCurrentSemester();
        return courseRepository.getCoursesBySemester(currentSemester);

    }

    private void navigateToMenu() {
        new MenuController();
    }

    private String getUserInput() {
        weeklyScheduleView.showQuitMessage();
        return TerminalManager.getInstance().read();
    }

    private void handleWeeklySchedule() {
        try {
            ArrayList<Course> currentCourses = fetchCourses();
            weeklyScheduleView.showWeeklySchedule(currentCourses);
            if (getUserInput().equals("q"))
                navigateToMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
