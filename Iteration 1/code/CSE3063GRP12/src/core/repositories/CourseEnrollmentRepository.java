package core.repositories;

import java.util.ArrayList;

import core.database.abstracts.DatabaseManager;
import core.enums.ApprovalState;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.general_providers.SessionController;
import core.models.abstracts.User;
import core.models.concretes.Advisor;
import core.models.concretes.Course;
import core.models.concretes.CourseEnrollment;

import java.io.IOException;

public class CourseEnrollmentRepository {
    private DatabaseManager databaseManager;
    private String path;

    public CourseEnrollmentRepository() {
        databaseManager = InstanceManager.getInstance().getDataBaseInstance();
        path = System.getProperty("user.dir") + AppConstant.getInstance().getBasePath() + "/course_enrollment/";
    }

    public CourseEnrollment getCourseEnrollmentByStudentId(String id) throws IOException {
        try {
            return databaseManager.read(path + "/" + id + ".json", CourseEnrollment.class);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public ArrayList<CourseEnrollment> getPendingEnrollments() {
        Advisor advisor = (Advisor) SessionController.getInstance().getCurrentUser();
        ArrayList<CourseEnrollment> courseEnrollments = new ArrayList<CourseEnrollment>();
        for (String studentId : advisor.getListOfStudentIds()) {
            try {
                CourseEnrollment courseEnrollment = databaseManager.read(path + "/" + studentId + ".json",
                        CourseEnrollment.class);
                if (courseEnrollment.getApprovalState() == ApprovalState.Pending) {
                    courseEnrollments.add(courseEnrollment);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return courseEnrollments;
    }

    public void updateEnrollment(String studentId, ApprovalState approvalState) throws IOException {
        CourseEnrollment courseEnrollment = databaseManager.read(path + "/" + studentId + ".json",
                CourseEnrollment.class);
        courseEnrollment.setApprovalState(approvalState);
        databaseManager.write(path + "/" + studentId + ".json", courseEnrollment);
    }

    public void createCourseEnrollment(ArrayList<Course> courses) throws IOException {
        User user = SessionController.getInstance().getCurrentUser();
        CourseEnrollment courseEnrollment = new CourseEnrollment("1", courses, "1", ApprovalState.Pending);
        databaseManager.write(path + "/" + user.getUserName() + ".json", courseEnrollment);
    }

}