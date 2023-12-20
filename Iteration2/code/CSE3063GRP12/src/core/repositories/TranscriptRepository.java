package core.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import core.database.abstracts.DatabaseManager;
import core.enums.CourseGrade;
import core.exceptions.UserNotFoundException;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.models.concretes.Transcript;
import core.models.concretes.Course;
import core.models.concretes.CourseEnrollment;
import core.models.concretes.Semester;
import core.models.concretes.Student;

public class TranscriptRepository {
    private DatabaseManager databaseManager;
    private UserRepository userRepository;

    private String path;

    public TranscriptRepository() {
        databaseManager = InstanceManager.getInstance().getDataBaseInstance();
        userRepository = new UserRepository();

        path = System.getProperty("user.dir") + AppConstant.getInstance().getBasePath() + "/transcript/";
    }

    public Transcript getTranscript(String studentId) throws IOException, UserNotFoundException {
        Student student = (Student) userRepository.getUser(studentId);
        int currentSemester = student.getTranscript().getCurrentSemester();
        return databaseManager.read(path + "/" + currentSemester + "/" + studentId + ".json", Transcript.class);
    }

    // TODO: IT SHOULD BE UPDATED BECAUSE UPDATE AND CREATE IS DIFFERENT
    public void updateTranscript(CourseEnrollment courseEnrollment)
            throws IOException, UserNotFoundException {
        Transcript transcript = getTranscript(courseEnrollment.getStudentId());
        int currentSemester = transcript.getCurrentSemester();

        if (transcript.getListOfSemesters() == null || currentSemester > transcript.getListOfSemesters().size()) {
            Map<String, CourseGrade> newCourseList = new HashMap<>();
            if (transcript.getListOfSemesters() == null) {
                transcript.setListOfSemesters(new HashMap<>());
            }
            for (Course course : courseEnrollment.getSelectedCourseList()) {
                newCourseList.put(course.getCourseCode(), CourseGrade.NON);
            }
            int totalCreditTaken = courseEnrollment.getSelectedCourseList().stream().mapToInt(Course::getCredit).sum();

            Semester semester = new Semester("0", newCourseList, totalCreditTaken, 0, transcript.getCurrentSemester());

            transcript.getListOfSemesters().put(currentSemester, semester);

            databaseManager.write(path + "/" + currentSemester + "/" + courseEnrollment.getStudentId() + ".json",
                    transcript);
        } else {
            // update current semester
            Map<String, CourseGrade> newCourseList = new HashMap<>();
            for (Course course : courseEnrollment.getSelectedCourseList()) {
                newCourseList.put(course.getCourseCode(), CourseGrade.NON);
            }

            int totalCreditTaken = courseEnrollment.getSelectedCourseList().stream().mapToInt(Course::getCredit).sum();

            Semester semester = new Semester("0", newCourseList, totalCreditTaken,
                    0, transcript.getCurrentSemester());
            transcript.getListOfSemesters().put(currentSemester, semester);
            // transcript.getListOfSemester().set(currentSemester - 1, semester);
            databaseManager.write(path + "/" + currentSemester + "/" + courseEnrollment.getStudentId() + ".json",
                    transcript);
        }

    }
}
