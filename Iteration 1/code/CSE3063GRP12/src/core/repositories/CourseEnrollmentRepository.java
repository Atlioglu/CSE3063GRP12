package core.repositories;

import java.util.ArrayList;

import core.database.abstracts.DatabaseManager;
import core.enums.ApprovalState;


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