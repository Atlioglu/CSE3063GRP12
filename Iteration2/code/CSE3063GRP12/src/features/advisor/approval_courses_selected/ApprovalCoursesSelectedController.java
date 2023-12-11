package features.advisor.approval_courses_selected;

import core.repositories.TranscriptRepository;
import features.main_menu.MenuController;
import core.repositories.CourseEnrollmentRepository;
import core.models.concretes.CourseEnrollment;

import java.io.IOException;

import core.enums.ApprovalState;
import core.exceptions.UserNotFoundException;
import core.general_providers.TerminalManager;

public class ApprovalCoursesSelectedController {
    private ApprovalCoursesSelectedView approvalCoursesSelectedView;
    private CourseEnrollment currentCourseEnrollment;
    private CourseEnrollmentRepository courseEnrollmentRepository;
    private TranscriptRepository transcriptRepository;

    public ApprovalCoursesSelectedController(CourseEnrollment currentCourseEnrollment) {
        this.currentCourseEnrollment = currentCourseEnrollment;
        approvalCoursesSelectedView = new ApprovalCoursesSelectedView();
        courseEnrollmentRepository = new CourseEnrollmentRepository();
        transcriptRepository = new TranscriptRepository();

        handleCourseApproval();
    }

    // TODO: HANDLE TRY CATCH
    private void handleCourseApproval() {
        try {

            approvalCoursesSelectedView.showSelectedCourses(currentCourseEnrollment.getSelectedCourseList());

            int input = getUserSelection();
            if (input == 1) {
                courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment.getStudentId(),
                        ApprovalState.Approved);
                updateTranscript(currentCourseEnrollment);
                approvalCoursesSelectedView.showSuccessMessage();
            } else
                courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment.getStudentId(),
                        ApprovalState.Rejected);
            navigateToMenu();

        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }

    }

    private int getUserSelection() {
        // if approve, press 1, reject press 0 for all courses
        System.out.print("Do you approve the previous registration? If yes, write 1. If no, write 0: ");
        String input = TerminalManager.getInstance().read();
        return Integer.parseInt(input);
    }

    private void updateTranscript(CourseEnrollment courseEnrollment) throws IOException, UserNotFoundException {
        transcriptRepository.updateTranscript(courseEnrollment);
    }

    // Go back to Menu by calling MenuController
    private void navigateToMenu() {
        new MenuController();
    }
}