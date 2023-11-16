package features.approval_courses_selected.approval_courses_selected_controller;

import core.repositories.TranscriptRepository;
import core.repositories.CourseEnrollmentRepository;
import features.approval_courses_selected.approval_courses_selected_view.ApprovalCoursesSelectedView;
import core.models.concretes.CourseEnrollment;
import core.enums.ApprovalState;
import core.general_providers.SessionController;
import core.general_providers.TerminalManager;

public class ApprovalCoursesSelectedController {
	private ApprovalCoursesSelectedView approvalCoursesSelectedView;
	private CourseEnrollment currentCourseEnrollment;
	private CourseEnrollmentRepository courseEnrollmentRepository;
	private TranscriptRepository transcriptRepository;
	
	public ApprovalCoursesSelectedController(CourseEnrollment currentCourseEnrollment) {
		this.currentCourseEnrollment = currentCourseEnrollment;
        handleCourseApproval();
	}
	
	private void handleCourseApproval() {
		approvalCoursesSelectedView.showSelectedCourses(currentCourseEnrollment.getSelectedCourseList());
        int input = getUserSelection();
        if(input == 1) {
			courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment.getStudentId(), ApprovalState.Approved);
            updateTranscript(currentCourseEnrollment);
            approvalCoursesSelectedView.showSuccessMessage();
        }
        else 
            courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment.getStudentId(), ApprovalState.Rejected);
	}
	
	private int getUserSelection() {
        // if approve, press 1, reject press 0 for all courses
        System.out.print("Do you approve the previous registration? If yes, write 1. If no, write 0: ");
        String input = TerminalManager.getInstance().read();
        return Integer.parseInt(input);
    }

    private void updateTranscript(CourseEnrollment courseEnrollment){
        transcriptRepository.updateTranscript(SessionController.getInstance().getCurrentUser(), courseEnrollment.getSelectedCourseList());
    }	

    // Go back to Menu by calling MenuController
	private void navigateToMenu() {
		MenuController();
    }
}
