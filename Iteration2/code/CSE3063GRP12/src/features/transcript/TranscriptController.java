package features.transcript;

import java.util.*;

import core.general_providers.SessionController;
import core.general_providers.TerminalManager;
import core.models.concretes.Course;
import core.models.concretes.Semester;
import core.models.concretes.Student;
import core.models.concretes.Transcript;
import core.repositories.CourseRepository;
import core.repositories.TranscriptRepository;
import features.main_menu.MenuController;

public class TranscriptController {
    private TranscriptView transcriptView;
    private TranscriptRepository transcriptRepository;
    private CourseRepository courseRepository;

    public TranscriptController() {
        this.transcriptView = new TranscriptView();
        this.transcriptRepository = new TranscriptRepository();
        this.courseRepository = new CourseRepository();
        handleTranscript();
    }

    private Transcript fetchTranscript(String studentId) {
        try {
            return transcriptRepository.getTranscript(studentId);
        } catch (Exception e) {
            return null;
        }

    }

    private String getUserInput() {
        return TerminalManager.getInstance().read();

    }

    private void navigateToMenu() {
        new MenuController();
    }

    private void handleTranscript() {
        try {
            SessionController sessionController = SessionController.getInstance();
            Transcript transcript = fetchTranscript(((Student) sessionController.getCurrentUser()).getUserName());

            Map<Integer, Semester> semesters = transcript.getListOfSemester();
            if (semesters == null) {
                System.out.println(
                        "Student doesn't have a semester");
                transcriptView.showQuitMessage();
                if (getUserInput().equals("q"))
                    navigateToMenu();

            } else {
                for (Semester semester : semesters.values()) {
                    System.out.println(
                            "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    ArrayList<String> courseIdList = new ArrayList<>(semester.getListOfCoursesTaken().keySet());
                    ArrayList<Course> courses = courseRepository.findCoursesWithCourseIds(courseIdList);

                    System.out.println("Course Code \t Course Name \t Course Credit \t Course Grade");
                    for (Course course : courses) {

                        System.out.println(course.getCourseCode() + "\t" + course.getName() + "\t" + course.getCredit()
                                + "\t" + semester.getListOfCoursesTaken().get(course.getCourseCode()));
                    }
                }
                System.out.println(
                        "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                transcriptView.showQuitMessage();
                if (getUserInput().equals("q"))
                    navigateToMenu();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}