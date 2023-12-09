package features.course_registration;

import core.repositories.TranscriptRepository;
import features.main_menu.MenuController;
import core.repositories.CourseRepository;
import core.enums.CourseGrade;
import core.exceptions.UserNotFoundException;
import core.exceptions.WrongNumberOfCoursesSelectedException;
import core.general_providers.SessionController;
import core.models.abstracts.User;
import core.models.concretes.Course;
import core.models.concretes.CourseEnrollment;
import core.models.concretes.Semester;
import core.models.concretes.Student;
import core.models.concretes.Transcript;
import core.repositories.CourseEnrollmentRepository;
import core.general_providers.TerminalManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CourseRegistrationController {
	private CourseRegistrationView courseRegistrationView;
	private TranscriptRepository transcriptRepository;
	private CourseRepository courseRepository;
	private CourseEnrollmentRepository courseEnrollmentRepository;

	public CourseRegistrationController() {
		this.courseRegistrationView = new CourseRegistrationView();
		this.transcriptRepository = new TranscriptRepository();
		this.courseRepository = new CourseRepository();
		this.courseEnrollmentRepository = new CourseEnrollmentRepository();
		handleCourseRegistration();
	}

	private void handleCourseRegistration() {
		try {
			User currentStudent = SessionController.getInstance().getCurrentUser();
			CourseEnrollment courseEnrollment = courseEnrollmentRepository
					.getCourseEnrollmentByStudentId(currentStudent.getUserName());
			if (courseEnrollment != null) {
				courseRegistrationView.showCourseList(courseEnrollment.getSelectedCourseList(),
						courseEnrollment.getApprovalState());
				getUserInput();
			} else {
				Transcript transcript = fetchTranscript(currentStudent.getUserName());
				ArrayList<Course> allCoursesPerSemester = fetchCoursesBySemester(transcript.getCurrentSemester());

				ArrayList<Course> availableCoursesForStudent = arrangeCoursesForStudent(transcript,
						allCoursesPerSemester,
						(Student) currentStudent);

				courseRegistrationView.showCourseList(availableCoursesForStudent);
				// get the courses selected by the student and send it to approval
				ArrayList<Course> courseListSelection = getUserSelections(availableCoursesForStudent);

				sendCoursesToApproval(courseListSelection);
				courseRegistrationView.showSuccessMessage();
				navigateToMenu();

			}

		} catch (Exception e) {
			courseRegistrationView.showErrorMessage(e);
		}

	}

	private Transcript fetchTranscript(String transcript) throws IOException, UserNotFoundException {
		return transcriptRepository.getTranscript(transcript);
	}

	private ArrayList<Course> fetchCoursesBySemester(int semester) throws IOException {
		return courseRepository.getCoursesBySemester(semester);
	}

	private ArrayList<Course> arrangeCoursesForStudent(Transcript transcript, ArrayList<Course> courseList,
			Student student) {

		/*
		 * we'll check the prerequisites (an arraylist) of the courses. If the student
		 * got FF on that prerequisite, then don't show
		 * the course of this semester. remove the course from the list
		 * We shouldn't take courses from upper semesters. Then call showCourseList
		 */
		// ArrayList<Semester> semester = transcript.getListOfSemester();

		Map<Integer, Semester> semester = transcript.getListOfSemester();

		ArrayList<Course> availableCourses = new ArrayList<>();
		for (Course courseThisSemester : courseList) {

			if (semester == null || semester.values() == null || semester.values().size() == 0) {
				availableCourses.add(courseThisSemester);
			} else {
				Semester currentSemester = semester.get(transcript.getCurrentSemester());
				Map<String, CourseGrade> listOfCoursesTaken = currentSemester.getListOfCoursesTaken();
				if (hasPassedPrerequisites(courseThisSemester.getPrerequisites(),
						listOfCoursesTaken))
					availableCourses.add(courseThisSemester);
			}

		}
		return availableCourses;
	}

	private boolean hasPassedPrerequisites(ArrayList<Course> prerequisites,
			Map<String, CourseGrade> listOfCoursesTaken) {
		CourseRepository courseRepository = new CourseRepository();
		ArrayList<String> idList = new ArrayList<>(listOfCoursesTaken.keySet());
		ArrayList<Course> courses = courseRepository.findCoursesWithCourseIds(idList);

		for (int i = 0; i < courses.size(); i++) {
			Course courseTaken = courses.get(i);
			CourseGrade grade = listOfCoursesTaken.get(courseTaken.getCourseCode());
			for (Course prerequisite : prerequisites) {
				if (Objects.equals(prerequisite, courseTaken)) {
					if (grade == CourseGrade.FF || grade == CourseGrade.FD) {
						System.out.println("has not Passed Prerequisites");
						return false;
					}
				}
			}
		}

		return true;
	}

	private ArrayList<Course> getUserSelections(ArrayList<Course> courseList) {
		ArrayList<Course> courseListSelection = new ArrayList<>();
		while (true) {
			try {
				System.out.print("Choose the index of the courses you want to enroll in: ");
				String selectedCourseIndex = TerminalManager.getInstance().read();

				String[] arraySelectedCourseIndicesString = selectedCourseIndex.split("[,\\s.]+");
				int[] arraySelectedCourseIndex = new int[arraySelectedCourseIndicesString.length];

				for (int i = 0; i < arraySelectedCourseIndicesString.length; i++) {
					arraySelectedCourseIndex[i] = Integer.parseInt(arraySelectedCourseIndicesString[i]);
				}
				if (arraySelectedCourseIndex.length != 5) {
					courseListSelection.clear(); // reuse the initialized arraylist
					throw new WrongNumberOfCoursesSelectedException();
				} else {
					for (Integer index : arraySelectedCourseIndex)
						courseListSelection.add(courseList.get(index - 1));
					// scanner.close();
					return courseListSelection;
				}
			} catch (WrongNumberOfCoursesSelectedException exception) {
				courseRegistrationView.showErrorMessage(exception);
			}
		}
	}

	private void getUserInput() {
		System.out.println("Press q to return to the menu");
		String input = TerminalManager.getInstance().read();
		if (input.length() == 1 && input.equals("q"))
			navigateToMenu();
	}

	private void sendCoursesToApproval(ArrayList<Course> courseEnrollment) {
		/* Sends courseList to CourseEnrollmentRepository */
		try {
			courseEnrollmentRepository.createCourseEnrollment(courseEnrollment);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// courseEnrollmentRepository.updateEnrollment((Student) currentStudent,
		// courseEnrollmentRepository.getCourseEnrollmentByStudentId(currentStudent.getId()));
	}

	// Go back to Menu by calling MenuController
	private void navigateToMenu() {
		// clearScreen();
		// TerminalManager.getInstance().dispose();
		new MenuController();
	}

	public void clearScreen() {

		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

}