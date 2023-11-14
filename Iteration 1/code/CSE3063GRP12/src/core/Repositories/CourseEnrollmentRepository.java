import java.util.ArrayList;
package repositories


public class CourseEnrollmentRepository{
    private DBManager dbManager;
    private String path;
    public CourseEnrollmentRepository(){

    }
    public CourseEnrollment getCourseEnrollmentByStudentId(int id){


        return null;
    }

    public ArrayList<CourseEnrollment> getPendingEnrollments(){

        return null;
    }

    public void updateEnrollment(String studentId, ApprovalState approvalState){

    }
}