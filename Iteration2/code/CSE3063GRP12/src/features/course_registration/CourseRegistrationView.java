package features.course_registration;

import java.util.ArrayList;

import core.enums.ApprovalState;
import core.models.concretes.Course;

public class CourseRegistrationView extends Exception {
	public void showCourseList(ArrayList<Course> courseList) {
		System.out.println("Your list of courses for this semester:");
		for (int i = 0; i < courseList.size(); i++) {
			System.out.println((i + 1) + ". " + courseList.get(i).getName());
		}
	}

	public void showCourseList(ArrayList<Course> courseList, ApprovalState approvalState) {
		/*
		 * If student sends his/her course list for approval and clicks again on the
		 * CourseRegistrationController,
		 * in the CourseEnrollmentRepository, there is a method
		 * getCourseEnrollmentByStudentId(String), we will get the CourseEnrollment
		 * model.
		 * Inside it, there are courseList, studentId, and approvalState. If the student
		 * didn't send anything to the advisor, then
		 * there will be no CourseEnrollment
		 * Must know whether the return type will be Null or CourseEnrollment
		 * CourseEnrollment courseEnrollment =
		 * courseRegistrationRepositry.getCourseEnrollmentByStudentId()
		 * if ce is true, courseEnrollment.getSelectedCourseList(),
		 * courseEnrollment.getApprovalState()
		 * print the courseList and printf("Your approval status is %s, ApprovalState)
		 * add, if you want to go back, press q
		 */
		System.out.println("Your list of courses for this semester:");
		for (int i = 0; i < courseList.size(); i++) {
			System.out.println((i + 1) + ". " + courseList.get(i).getName());
		}
		System.out.printf("Your registration status is: %s \n", approvalState);

	}

	public void showSuccessMessage() {
		System.out.println("Your enrollment has been saved successfully");
	}

	public void showErrorMessage(Exception exception) {
		System.err.println(exception.getMessage());
	}
}
