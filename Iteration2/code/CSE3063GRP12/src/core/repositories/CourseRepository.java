package core.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.database.abstracts.DatabaseManager;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.models.concretes.Course;
import core.models.concretes.CourseEnrollment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CourseRepository {
    private DatabaseManager databaseManager;
    private String path;

    public CourseRepository() {
        databaseManager = InstanceManager.getInstance().getDataBaseInstance();
        path = System.getProperty("user.dir") + AppConstant.getInstance().getBasePath() + "/course/";
    }

    public ArrayList<Course> getCoursesBySemester(int id) throws IOException {
        return readAllJsonFilesInFolder(path + id + "/");
    }

    public void createCourse(Course course) throws IOException {
        databaseManager.write(path + course.getSemester() + "/" + course.getCourseCode() + ".json", course);
    }

    public ArrayList<Course> readAllJsonFilesInFolder(String folderPath) {
        ArrayList<Course> courses = new ArrayList<>();
        Path path = Paths.get(folderPath);
        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json")) // Sadece .json uzantılı dosyaları filtrele
                    .forEach(file -> {
                        Course course = null;
                        try {
                            course = databaseManager.read(file.toString(), Course.class);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (course != null) {
                            courses.add(course);
                        }
                    });
        } catch (IOException e) {
            // e.printStackTrace();
        }

        return courses;
    }

    public int getQuota(String courseCode) throws IOException {
        Course course = findCourseByCode(courseCode);

        if (course != null) {
            return course.getQuota();
        } else {
            throw new IllegalArgumentException("Course with code " + courseCode + " not found.");
        }
    }

    public int getCurrentQuota(String courseCode) throws IOException {
        Course course = findCourseByCode(courseCode);

        if (course != null) {
            return course.getCurrentQuota();
        } else {
            throw new IllegalArgumentException("Course with code " + courseCode + " not found.");
        }
    }

    public Course findCourseByCode(String courseCode) throws IOException {
        Path startPath = Paths.get(path);
        try {
            return Files.walk(startPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .filter(path -> path.getFileName().toString().contains(courseCode))
                    .findFirst()
                    .map(path -> {
                        try {
                            return databaseManager.read(path.toString(), Course.class);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return null;
                        }
                    })
                    .orElse(null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Course> findCoursesWithCourseIds(ArrayList<String> ids) {

        ArrayList<Course> matchedCourses = new ArrayList<>();
        Path startPath = Paths.get(path);
        try {
            Files.walk(startPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .filter(path -> ids.stream().anyMatch(id -> path.getFileName().toString().contains(id)))
                    .forEach(path -> {
                        try {
                            Course course = databaseManager.read(path.toString(), Course.class);
                            matchedCourses.add(course);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }

        return matchedCourses;
    }

    public void updateCurrentQuota(CourseEnrollment courseEnrollment) throws IOException {
        if (courseEnrollment == null) {
            System.out.println("CourseEnrollment is null.");
            return;
        }

        //Test
        //System.out.println("updateCurrentQuota is called.");

        ArrayList<Course> approvedCourses = courseEnrollment.getApprovedCourseList();

        if (approvedCourses == null || approvedCourses.isEmpty()) {
            System.out.println("No approved courses found for the student.");
            return;
        }

        for (Course selectedCourse : approvedCourses) {
            String courseCode = selectedCourse.getCourseCode();
            System.out.println("Processing course with code: " + courseCode);

            // Find the corresponding course in the repository
            Course repositoryCourse = findCourseByCode(courseCode);

            if (repositoryCourse != null) {
                // Update the currentQuota for the course in the repository
                int currentQuota = repositoryCourse.getCurrentQuota();
                int updatedQuota = currentQuota + 1;

                //Test
                // System.out.println("Current Quota: " + currentQuota);
                // System.out.println("Updated Quota: " + updatedQuota);

                repositoryCourse.setCurrentQuota(updatedQuota);

                // Save the updated course back to the repository
                databaseManager.write(
                        path + repositoryCourse.getSemester() + "/" + repositoryCourse.getCourseCode() + ".json",
                        repositoryCourse);

                //Test
                //System.out.println("Quota updated successfully for course: " + courseCode);
            } else {
                System.out.println("Course with code " + courseCode + " not found in the repository.");
            }
        }

    }
}