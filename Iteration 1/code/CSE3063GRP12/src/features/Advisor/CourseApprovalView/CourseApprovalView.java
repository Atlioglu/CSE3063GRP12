package features.Advisor.CourseApprovalView;

public class CourseApprovalView {
    public void showPendingCourseEnrollments(ArrayList<CourseEnrollment> courseEnrollmentArrayList){
        for(int i = 0;i<courseEnrollmentArrayList.size();i++){
            System.out.printf(courseEnrollmentArrayList.get(i).getSelectedCourseList());
            System.out.printf("\s");
    }
}
