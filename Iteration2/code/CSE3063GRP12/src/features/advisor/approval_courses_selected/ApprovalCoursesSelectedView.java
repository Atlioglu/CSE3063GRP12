package features.advisor.approval_courses_selected;

import java.util.ArrayList;
import core.models.concretes.Course;

public class ApprovalCoursesSelectedView {

	public void showSelectedCourses(ArrayList<Course> selectedCourseList) {
    System.out.println("====================================================|| Registration Management ||====================================================");
    System.out.println("###======List of Student Selected Courses:");
    System.out.println("=====================================================================================================================================");
    System.out.printf("%-3s\t%-20s %-50s %-8s %-8s %-8s\n", " No.", "Course Code", "Course Name", "Quota", "Semester No.", "ECTS");
    System.out.println("=====================================================================================================================================");

    for (int i = 0; i < selectedCourseList.size(); i++) {
        Course currentCourse = selectedCourseList.get(i);
        System.out.printf(" [%-1d]\t%-20s %-52s %-12d %-8d %-8d\n",
        i + 1, currentCourse.getCourseCode(), currentCourse.getName(), 
		currentCourse.getQuota(), currentCourse.getSemester(), currentCourse.getCredit());
    }

    System.out.println("=====================================================================================================================================");
	}

	public void showSuccessMessage() {
		System.out.println("You have successfully approved the course list");
	}

	public void showErrorMessage(Exception exception) {
		System.err.println(exception);
	}
}
