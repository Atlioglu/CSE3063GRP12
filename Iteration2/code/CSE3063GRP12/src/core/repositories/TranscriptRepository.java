package core.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import core.database.abstracts.DatabaseManager;
import core.enums.CourseGrade;
import core.exceptions.UserNotFoundException;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.general_providers.SessionController;
import core.models.abstracts.User;
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

    public void createTranscript(Transcript transcript) throws IOException {

        databaseManager.write(path + "/" + SessionController.getInstance().getCurrentUser().getUserName() + ".json",
                transcript);
    }

    // TODO: WHY WE NEED THIS?
    public ArrayList<Course> getCoursesForSemesterByStudent(User user) throws IOException {
        Transcript transcript = databaseManager.read(path + "/" + user.getUserName()
                + ".json", Transcript.class);

        ArrayList<Semester> semesters = transcript.getListOfSemester();

        Map<String, CourseGrade> coursesMap = semesters.get(semesters.size() -
                1).getListOfCoursesTaken();
        // return new ArrayList<>(coursesMap.keySet());
        return new ArrayList<>();
    }

    public List<String> findFilesWithId(String directoryPath, String id) {
        String pa = System.getProperty("user.dir") + AppConstant.getInstance().getBasePath() + "/course/";
        List<String> matchedFiles = new ArrayList<>();
        Path startPath = Paths.get(pa);

        try {
            Files.walk(startPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .filter(path -> path.getFileName().toString().contains(id))
                    .forEach(path -> matchedFiles.add(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchedFiles;
    }

    public ArrayList<Course> _fetchCourseListByStudentListId(ArrayList<String> id) {

        return new ArrayList<>();
    }

    // TODO: IT SHOULD BE UPDATED BECAUSE UPDATE AND CREATE IS DIFFERENT
    public void updateTranscript(CourseEnrollment courseEnrollment)
            throws IOException, UserNotFoundException {
        // int currentSemester = student.getTranscript().getCurrentSemester();
        // Transcript transcript = student.getTranscript();
        Transcript transcript = getTranscript(courseEnrollment.getStudentId());
        int currentSemester = transcript.getCurrentSemester();

        if (currentSemester > transcript.getListOfSemester().size()) {
            Map<String, CourseGrade> newCourseList = new HashMap<>();
            for (Course course : courseEnrollment.getSelectedCourseList()) {
                newCourseList.put(course.getCourseCode(), CourseGrade.NON);
            }
            int totalCreditTaken = courseEnrollment.getSelectedCourseList().stream().mapToInt(Course::getCredit).sum();

            Semester semester = new Semester("0", newCourseList, totalCreditTaken, 0);
            transcript.getListOfSemester().add(semester);
            databaseManager.write(path + "/" + courseEnrollment.getStudentId() + ".json",
                    transcript);
        } else {
            // update current semester
            Map<String, CourseGrade> newCourseList = new HashMap<>();
            for (Course course : courseEnrollment.getSelectedCourseList()) {
                newCourseList.put(course.getCourseCode(), CourseGrade.NON);
            }

            int totalCreditTaken = courseEnrollment.getSelectedCourseList().stream().mapToInt(Course::getCredit).sum();

            Semester semester = new Semester("0", newCourseList, totalCreditTaken,
                    0);
            transcript.getListOfSemester().set(currentSemester - 1, semester);
            databaseManager.write(path + "/" + courseEnrollment.getStudentId() + ".json",
                    transcript);
        }

    }
}
