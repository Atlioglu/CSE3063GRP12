package features.Advisor;

import core.general_providers.TerminalManager;
import core.repositories.CourseEnrollmentRepository;
import core.models.concretes.CourseEnrollment;
import core.exceptions.UnexpectedInputException;
import features.main_menu.MenuController;
import java.util.ArrayList;

public class CourseApprovalController {
    private CourseApprovalView courseApprovalView;
    private CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseApprovalController() {
        courseEnrollmentRepository = new CourseEnrollmentRepository();
        courseApprovalView = new CourseApprovalView();

        handleApprovalController();
    }

    private ArrayList<CourseEnrollment> fetchPendingEnrollments() {
        return courseEnrollmentRepository.getPendingEnrollments();
    }

    // TODO: Handle unexpected input
    private int getUserSelection() throws UnexpectedInputException{
        System.out.println("Type \"q\" to see the menu\nType Student Id to process course enrollment");
        ArrayList<CourseEnrollment> courseEnrollmentList = fetchPendingEnrollments();
        courseApprovalView.showPendingCourseEnrollments(courseEnrollmentList);
        String input = TerminalManager.getInstance().read();
        TerminalManager.getInstance().dispose();
        if (input.equals("q")) {
            return -1;
        } else {
            for (int i = 0; i < courseEnrollmentList.size(); i++) {
                if(courseEnrollmentList.get(i).getStudentId().equals(input)) {
                    return i;
                }
            }
        }
        throw new UnexpectedInputException();
    }

    private void navigateToApprovalCourses(CourseEnrollment courseEnrollment) {
      //  new ApprovalCoursesSelected(courseEnrollment);
    }

    private void navigateToMenu() {
        new MenuController();
    }

    private void handleApprovalController() {
        int selection;
        while (true) {
            try {
                selection = getUserSelection();
            }
            catch (UnexpectedInputException e){
                courseApprovalView.showErrorMessage(e);
                selection = -2;
            }
            if (selection == -1) {
                navigateToMenu();
                break;
            } else if (selection >= 0) {
                navigateToApprovalCourses(fetchPendingEnrollments().get(selection));
                break;
            }
        }
    }
}
