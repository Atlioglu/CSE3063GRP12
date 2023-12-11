package features.advisor.approval_courses_selected;

import core.repositories.TranscriptRepository;
import features.main_menu.MenuController;
import core.repositories.CourseEnrollmentRepository;
import core.models.concretes.Course;
import core.models.concretes.CourseEnrollment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import core.enums.ApprovalState;
import core.exceptions.UnexpectedInputException;
import core.exceptions.UserNotFoundException;
import core.exceptions.WrongNumberOfCoursesSelectedException;
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

    private void handleCourseApproval() {
        try {

            approvalCoursesSelectedView.showSelectedCourses(currentCourseEnrollment.getSelectedCourseList());

            ArrayList<Course>[] allCourses = getAdvisorCourseSelections(
                    currentCourseEnrollment.getSelectedCourseList());
            ArrayList<Course> approvedCourses = allCourses[0];
            ArrayList<Course> rejectedCourses = allCourses[1];

            if (approvedCourses.size() == currentCourseEnrollment.getSelectedCourseList().size()) {
                // all approved
                courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment.getStudentId(),
                        approvedCourses,
                        rejectedCourses,
                        ApprovalState.Approved);
                updateTranscript(currentCourseEnrollment);
            } else {
                // some approved some rejected
                courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment.getStudentId(),
                        approvedCourses,
                        rejectedCourses,
                        ApprovalState.Rejected);
            }
            navigateToMenu();

        } catch (Exception e) {
            approvalCoursesSelectedView.showErrorMessage(e);
        }

    }

    private ArrayList<Course>[] getAdvisorCourseSelections(ArrayList<Course> courseList) throws IOException {
        ArrayList<Course> approvedCourses = new ArrayList<>();
        ArrayList<Course> rejectedCourses = new ArrayList<>();

        while (true) {
            System.out.print("Which courses do you want to approve? (0 for none): ");
            String selectedCourseIndexes = TerminalManager.getInstance().read();

            if (selectedCourseIndexes.length() == 1 && selectedCourseIndexes.equals("0")) {
                // all rejected
                rejectedCourses.addAll(courseList);
                return new ArrayList[] { approvedCourses, rejectedCourses };
            }

            try {
                if (selectedCourseIndexes.length() < 1 || !selectedCourseIndexes.matches("^[0-9\\s,]*$")) {
                    // empty input or alphabetic input
                    throw new UnexpectedInputException();
                }

                String[] arraySelectedCourseIndicesString = selectedCourseIndexes.split("[,\\s.]+");
                Arrays.sort(arraySelectedCourseIndicesString);
                int iterApprovedCoursesIndex = 0;
                for (int i = 0; i < courseList.size(); i++) {
                    if(iterApprovedCoursesIndex < arraySelectedCourseIndicesString.length){
                        if (Integer.parseInt(arraySelectedCourseIndicesString[iterApprovedCoursesIndex]) == i + 1) {
                            approvedCourses.add(courseList.get(i));
                            iterApprovedCoursesIndex++;
                        }else {
                            rejectedCourses.add(courseList.get(i));
                        }
                    }
                    else {
                        rejectedCourses.add(courseList.get(i));
                    }
                }
                return new ArrayList[] { approvedCourses, rejectedCourses };
            } catch (UnexpectedInputException exception) {
                approvalCoursesSelectedView.showErrorMessage(exception);

            }
        }

    }

    private void updateTranscript(CourseEnrollment courseEnrollment) throws IOException, UserNotFoundException {
        transcriptRepository.updateTranscript(courseEnrollment);
    }

    // Go back to Menu by calling MenuController
    private void navigateToMenu() {
        new MenuController();
    }
}