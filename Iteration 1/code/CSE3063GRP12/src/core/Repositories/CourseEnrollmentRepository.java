package core.repositories;

import java.util.ArrayList;
import core.Enums.ApprovalState;
import core.database.abstracts.DatabaseManager;


public class CourseEnrollmentRepository{
    private DatabaseManager dbManager;
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