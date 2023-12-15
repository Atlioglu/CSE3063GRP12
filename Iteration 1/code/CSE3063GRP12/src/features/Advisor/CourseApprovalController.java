package features.advisor;

import core.general_providers.TerminalManager;
import core.repositories.CourseEnrollmentRepository;
import core.models.concretes.CourseEnrollment;
import core.exceptions.UnexpectedInputException;
import features.approval_courses_selected.ApprovalCoursesSelectedController;
import features.main_menu.MenuController;
import java.util.ArrayList;

public class CourseApprovalController {
    private CourseApprovalView courseApprovalView;
    private CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseApprovalController() {
        courseEnrollmentRepository = new CourseEnrollmentRepository();
        courseApprovalView = new CourseApprovalView();

        try {
            handleApprovalController();
        } catch (UnexpectedInputException e) {
            courseApprovalView.showErrorMessage(e);
        }
    }

    private ArrayList<CourseEnrollment> fetchPendingEnrollments() {
        return courseEnrollmentRepository.getPendingEnrollments();

    }

    private void navigateToApprovalCoursesSelected(CourseEnrollment courseEnrollment) {

        new ApprovalCoursesSelectedController(courseEnrollment);
    }

    private void navigateToMenu() {
        new MenuController();
    }

    private String getUserInput() {
        String input = TerminalManager.getInstance().read();
        // TerminalManager.getInstance().dispose();
        return input;
    }

    private void handleApprovalController() throws UnexpectedInputException {
        ArrayList<CourseEnrollment> pendingEnrollments = fetchPendingEnrollments();
        courseApprovalView.showPendingCourseEnrollments(pendingEnrollments);

        courseApprovalView.showPromptMessage();
        String selection = getUserInput();
        if (selection.matches("^\\d+$") == false && selection.equals("q") == false) {
            throw new UnexpectedInputException();
        } else if (selection.equals("q")) {
            navigateToMenu();
        } else {
            int index = Integer.parseInt(selection);
            if (index > courseEnrollmentRepository.getPendingEnrollments().size() || index < 0) {
                throw new UnexpectedInputException();
            } else {
                navigateToApprovalCoursesSelected(courseEnrollmentRepository.getPendingEnrollments().get(index - 1));
            }
        }
    }
}
