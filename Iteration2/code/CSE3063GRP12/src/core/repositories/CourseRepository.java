package core.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.database.abstracts.DatabaseManager;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.models.concretes.Course;
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

}