package features.approval_courses_selected.approval_courses_selected_view;

import java.util.ArrayList;
import core.models.concretes.Course;

public class ApprovalCoursesSelectedView {
	
	public void showSelectedCourses(ArrayList<Course> selectedCourseList) {
		System.out.println("Your selected courses are:");
		for(int i = 0; i < selectedCourseList.size(); i++) 
			System.out.println((i+1) + ". " + selectedCourseList.get(i).getName());
	}
	
	public void showSuccessMessage() {
		System.out.println("You have successfully approved the course list");
	}
	
	public void showErrorMessage(Exception exception) {
		System.err.println(exception);
	}
}
