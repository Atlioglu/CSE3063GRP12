package features.Advisor;

import core.models.concretes.CourseEnrollment;
import core.exceptions.UnexpectedInputException;
import java.util.ArrayList;

public class CourseApprovalView {
    public void showPendingCourseEnrollments(ArrayList<CourseEnrollment> courseEnrollmentArrayList){
        for(int i = 0;i<courseEnrollmentArrayList.size();i++) {
            System.out.printf(courseEnrollmentArrayList.get(i).getStudentId());
        }
    }

    public void showErrorMessage(Exception e){
       new UnexpectedInputException();
    }
}
