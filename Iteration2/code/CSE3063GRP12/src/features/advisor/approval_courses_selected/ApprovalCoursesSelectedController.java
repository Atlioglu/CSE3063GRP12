package features.advisor.approval_courses_selected;

import core.repositories.TranscriptRepository;
import features.main_menu.MenuController;
import core.repositories.CourseEnrollmentRepository;
import core.models.concretes.Course;
import core.models.concretes.CourseEnrollment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import core.repositories.NotificationRepositories;
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
    private NotificationRepositories notificationRepositories;

    public ApprovalCoursesSelectedController(CourseEnrollment currentCourseEnrollment) {
        this.currentCourseEnrollment = currentCourseEnrollment;
        approvalCoursesSelectedView = new ApprovalCoursesSelectedView();
        courseEnrollmentRepository = new CourseEnrollmentRepository();
        transcriptRepository = new TranscriptRepository();
		notificationRepositories = new NotificationRepositories();

        handleCourseApproval();
    }

    private void handleCourseApproval() {
        try {
             // Filter out courses that are already approved
            ArrayList<Course> coursesToReview = new ArrayList<>();
            for (Course course : currentCourseEnrollment.getSelectedCourseList()) {
                if (!isCourseInList(course, currentCourseEnrollment.getApprovedCourseList()) &&
                    !isCourseInList(course, currentCourseEnrollment.getRejectedCourseList())) {
                    coursesToReview.add(course);
                }
            }

            // Show the filtered courses to the advisor
            approvalCoursesSelectedView.showSelectedCourses(coursesToReview);

            ArrayList<Course>[] allCourses = getAdvisorCourseSelections(
                    coursesToReview);
            ArrayList<Course> approvedCourses = allCourses[0];
            ArrayList<Course> rejectedCourses = allCourses[1];

            if (approvedCourses.size() == coursesToReview.size()) {
                // all approved, first enrollment
                if(currentCourseEnrollment.getApprovedCourseList() == null){
                    courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment, currentCourseEnrollment.getStudentId(),
                        approvedCourses,
                        rejectedCourses,
                        ApprovalState.Approved);
                }
                else{
                    currentCourseEnrollment.getApprovedCourseList().addAll(approvedCourses);
                    courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment,currentCourseEnrollment.getStudentId(),
                            currentCourseEnrollment.getApprovedCourseList(),
                            currentCourseEnrollment.getRejectedCourseList(),
                            ApprovalState.Approved);
                }
                updateTranscript(currentCourseEnrollment);
                sendNotification("accepted");

            } else {
                // some approved some rejected and has previous approved/rejected courses
                if(currentCourseEnrollment.getApprovedCourseList() != null && currentCourseEnrollment.getRejectedCourseList() != null){
                    currentCourseEnrollment.getApprovedCourseList().addAll(approvedCourses);
                    currentCourseEnrollment.getRejectedCourseList().addAll(rejectedCourses);
                    courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment,currentCourseEnrollment.getStudentId(),
                            currentCourseEnrollment.getApprovedCourseList(),
                            currentCourseEnrollment.getRejectedCourseList(),
                            ApprovalState.Rejected);
                    sendNotification("rejected");
                }

                else{
                    // some approved som rejected, first enrollment
                    courseEnrollmentRepository.updateEnrollment(currentCourseEnrollment, currentCourseEnrollment.getStudentId(),
                        approvedCourses,
                        rejectedCourses,
                        ApprovalState.Rejected);
                    sendNotification("rejected");

                    }
            }
            navigateToMenu();

        } catch (Exception e) {
            approvalCoursesSelectedView.showErrorMessage(e);
        }

    }

    private void sendNotification(String message) {
        notificationRepositories.updateNotification(currentCourseEnrollment.getStudentId(), "Your requeste is "+ message);
	}

    private boolean isCourseInList(Course course, ArrayList<Course> courseList) {
        return courseList != null && courseList.stream()
                .anyMatch(existingCourse -> existingCourse.getId().equals(course.getId()));
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
                Arrays.sort(arraySelectedCourseIndicesString, Comparator.comparingInt(Integer::parseInt));
                int iterApprovedCoursesIndex = 0;
                for (int i = 0; i < courseList.size(); i++) {
                    if (iterApprovedCoursesIndex < arraySelectedCourseIndicesString.length &&
                        Integer.parseInt(arraySelectedCourseIndicesString[iterApprovedCoursesIndex]) == i + 1) {
                        // Course is approved
                        approvedCourses.add(courseList.get(i));
                        iterApprovedCoursesIndex++;
                    } else {
                        // Course is rejected
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