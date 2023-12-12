package features.student.course_registration;

import core.repositories.TranscriptRepository;
import features.main_menu.MenuController;
import core.repositories.CourseRepository;
import core.enums.ApprovalState;
import core.enums.CourseGrade;
import core.exceptions.UserNotFoundException;
import core.exceptions.WrongNumberOfCoursesSelectedException;
import core.exceptions.UnexpectedInputException;
import core.general_providers.SessionController;
import core.models.abstracts.User;
import core.models.concretes.Course;
import core.models.concretes.CourseEnrollment;
import core.models.concretes.Semester;
import core.models.concretes.Transcript;
import core.repositories.CourseEnrollmentRepository;
import core.general_providers.TerminalManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

			if (courseEnrollment != null && courseEnrollment.getApprovalState() != ApprovalState.Rejected) {
				// Handle existing CourseEnrollment
				handleExistingCourseEnrollment(courseEnrollment);
			} else{
				// Handle null or rejected CourseEnrollment
				if(courseEnrollment == null)
					handleNullOrRejectedCourseEnrollment(null, currentStudent);
				else
					handleNullOrRejectedCourseEnrollment(courseEnrollment, currentStudent);
			}
		} catch (Exception e) {
			courseRegistrationView.showErrorMessage(e);
		}
	}

	private void handleExistingCourseEnrollment(CourseEnrollment courseEnrollment) {
		courseRegistrationView.showCourseList(courseEnrollment.getSelectedCourseList(),
				courseEnrollment.getApprovalState());
				// ask the user to add/drop courses and send the rest of the courseEnrollment to the advisor
				
		getUserInput();
	}

	private void handleNullOrRejectedCourseEnrollment(CourseEnrollment courseEnrollment, User currentStudent) {
		try {
			Transcript transcript = fetchTranscript(currentStudent.getUserName());
			ArrayList<Course> allCoursesPerSemester = fetchCoursesBySemester(transcript.getCurrentSemester());
	
			ArrayList<Course> availableCoursesForStudent = arrangeCoursesForStudent(transcript, allCoursesPerSemester);
			
			// Combine rejected courses with courses not selected by the student
				ArrayList<Course> allCourses = new ArrayList<>();

			if (courseEnrollment != null && courseEnrollment.getApprovalState() == ApprovalState.Rejected) {
				System.out.println("---------------------------------------------");
				System.out.println("Your enrollment was rejected. Reapply again");
				System.out.println("---------------------------------------------");
	
				// Combine rejected courses with courses not selected by the student
				allCourses = new ArrayList<>();
				allCourses.addAll(getCoursesNotSelectedByStudent(courseEnrollment.getApprovedCourseList(), availableCoursesForStudent));
				allCourses.addAll(getRetakeCourses(transcript));

				System.out.println("Your approved courses are: ");
				courseRegistrationView.showCourseList(courseEnrollment.getApprovedCourseList());
				System.out.println();

				// Show rejected courses along with other courses
				courseRegistrationView.showCourseList(allCourses);
			} 
			else {
				// if the student didn't enroll, show all available courses including failed, or need-to-repeat courses
				allCourses.addAll(availableCoursesForStudent);
				allCourses.addAll(getRetakeCourses(transcript));
				courseRegistrationView.showCourseList(allCourses);

			}
	
			// Get the courses selected by the student and send them to approval
			ArrayList<Course> courseListSelection = getUserSelections(allCourses);
	
			// Add/drop courses before sending them to the advisor
			addDropCoursesOptions(courseListSelection, allCourses);
	
			if (!courseListSelection.isEmpty()) {
				sendCoursesToApproval(courseListSelection);
				courseRegistrationView.showSuccessMessage();
			}
			navigateToMenu();
		} catch (IOException | UserNotFoundException e) {
			courseRegistrationView.showErrorMessage(e);
		}
	}
	
	// Helper method to get courses that are not selected by the student
	private ArrayList<Course> getCoursesNotSelectedByStudent(ArrayList<Course> selectedCourses, ArrayList<Course> allCourses) {
		ArrayList<Course> notSelectedCourses = new ArrayList<>();
	
		for (Course course : allCourses) {
			if (!containsCourseWithId(selectedCourses, course)) {
				notSelectedCourses.add(course);
			}
		}
	
		return notSelectedCourses;
	}
	
	// Helper method to check if a list contains a course with a specific id
	private boolean containsCourseWithId(ArrayList<Course> courses, Course targetCourse) {
		for (Course course : courses) {
			// Use a unique identifier (e.g., course code) to compare courses
			if (course.getId().equals(targetCourse.getId())) {
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<Course> getRetakeCourses(Transcript transcript) {
		ArrayList<String> retakeCourseIds = findRetakeCourseIds(transcript);
		return courseRepository.findCoursesWithCourseIds(retakeCourseIds);
	}
	
	private ArrayList<String> findRetakeCourseIds(Transcript transcript) {
		ArrayList<String> retakeCourseIds = new ArrayList<>();
	
		for (Map.Entry<Integer, Semester> entry : transcript.getListOfSemesters().entrySet()) {
			Semester semester = entry.getValue();
			for (Map.Entry<String, CourseGrade> courseGradeEntry : semester.getListOfCoursesTaken().entrySet()) {
				String courseId = courseGradeEntry.getKey();
				CourseGrade grade = courseGradeEntry.getValue();
				
				// Check if the grade is DD or DC, and add the courseId to retakeCourseIds
				if (grade == CourseGrade.DD || grade == CourseGrade.DC || grade == CourseGrade.FF) {
					retakeCourseIds.add(courseId);
				}
			}
		}
	
		return retakeCourseIds;
	}

	private Transcript fetchTranscript(String transcript) throws IOException, UserNotFoundException {
		return transcriptRepository.getTranscript(transcript);
	}

	private ArrayList<Course> fetchCoursesBySemester(int semester) throws IOException {
		return courseRepository.getCoursesBySemester(semester);
	}

	private ArrayList<Course> arrangeCoursesForStudent(Transcript transcript, ArrayList<Course> currentSemesterCourseList) {
		Map<Integer, Semester> semesters = transcript.getListOfSemesters();
		ArrayList<Course> availableCourses = new ArrayList<>();

		for (Course courseThisSemester : currentSemesterCourseList) {
			if (semesters == null || semesters.values() == null || semesters.values().size() == 0) {
				availableCourses.add(courseThisSemester);
			} else {
				// Check if the course has prerequisites, and if the student has received FF or FD in any of them, add them to the list
				if (courseThisSemester.getPrerequisites() != null && !courseThisSemester.getPrerequisites().isEmpty()) {
					addFailedPrerequisites(courseThisSemester.getPrerequisites(), semesters, availableCourses);
				} 
				// if the course does not have any prerequisites, add it
				else availableCourses.add(courseThisSemester); 
			}
		}	
		return availableCourses;
	}
	
	private void addFailedPrerequisites(ArrayList<Course> prerequisites, Map<Integer, Semester> semesters,
					ArrayList<Course> availableCourses) {
		for (Course prerequisite : prerequisites) {
			if (hasFailedCourse(prerequisite.getCourseCode(), semesters)) {
				availableCourses.add(prerequisite); // Add each failed prerequisite
			}
		}
}
	private boolean hasFailedCourse(String courseCode, Map<Integer, Semester> semesters) {
		CourseGrade grade;
		for (Semester semester : semesters.values()) {
			grade = semester.getListOfCoursesTaken().get(courseCode);
			if (grade == CourseGrade.FF || grade == CourseGrade.FD) {
				return true; // Student has failed the course by getting FF or FD
			}
		}
		return false; // Student has not failed the course
	}

	private void addDropCoursesOptions(ArrayList<Course> courseList, ArrayList<Course> availableCourses) {
		boolean validInput = false;
		while (!validInput) {
			try {
				String decision;
				System.out.print("Do you want to add or drop any courses? (type 'add', 'drop', or 'done') ");
				decision = TerminalManager.getInstance().read().toLowerCase();
	
				if (decision.equals("add")) {
					addCourseOption(courseList, availableCourses);
				} else if (decision.equals("drop")) {
					dropCourseOption(courseList);
				} else if (decision.equals("done")) {
					validInput = true;
				} else {
					throw new UnexpectedInputException();
				}
				if(courseList.size() > 0){
					System.out.print("Do you want to send your enrollment to your advisor? (yes/no) ");
					decision = TerminalManager.getInstance().read().toLowerCase();
					
					if(decision.equals("yes")) {
						validInput = true;
						sendCoursesToApproval(courseList);
						courseRegistrationView.showSuccessMessage();
						getUserInput();
					}
					else if(decision.equals("no")) getUserInput(); // go back to menu ------
					else throw new UnexpectedInputException();
				}
			} catch (UnexpectedInputException exception) {
				courseRegistrationView.showErrorMessage(exception);
				continue; // reprompt user
			}
		}
	}

	private void addCourseOption(ArrayList<Course> courseList, ArrayList<Course> availableCourses) {
		if(courseList.size() == availableCourses.size()){
			System.out.println("You have selected all courses!");
			return;
		}
			// Create a new list to hold courses that are not in courseList
			ArrayList<Course> coursesToDisplay = new ArrayList<>(availableCourses);
		
			// Remove courses that are already in courseList
			coursesToDisplay.removeAll(courseList);
			courseRegistrationView.showCourseList(coursesToDisplay);
			// get the courses selected by the student, concatenate all courses selected and send it to approval
			ArrayList<Course> courseListSelection = getUserSelections(coursesToDisplay);
			courseList.addAll(courseListSelection);
	}
	
	private void dropCourseOption(ArrayList<Course> courseList) {
		if (courseList.size() == 0) {
			System.out.println("You don't have any courses to drop!");
			return;
		}

		boolean isValid = false;
		while (!isValid) {
			courseRegistrationView.showCourseList(courseList);
			System.out.print("Which courses do you want to drop? ");
			String selection = TerminalManager.getInstance().read();
			String[] selectionArray = selection.split("[,\\s.]+");
	
			Set<Integer> indicesToRemove = new HashSet<>();

        	try {
				for (String selectionIndex : selectionArray) {
					try {
						int index = Integer.parseInt(selectionIndex);
						if (index > 0 && index <= courseList.size()) {
							indicesToRemove.add(index - 1);
						} else {
							throw new UnexpectedInputException();
						}
					} catch (NumberFormatException e) {
						throw new UnexpectedInputException();
					}
				}

				// Check for repetitions
				if (indicesToRemove.size() != selectionArray.length) {
					throw new UnexpectedInputException();
				}

				isValid = true;

			} catch (UnexpectedInputException exception) {
				courseRegistrationView.showErrorMessage(exception);
			}

			// Remove the selected courses only if input is valid
			if (isValid) {
				List<Integer> indicesToRemoveList = new ArrayList<>(indicesToRemove);
				Collections.sort(indicesToRemoveList, Collections.reverseOrder());

				for (int indexToRemove : indicesToRemoveList) {
					courseList.remove(indexToRemove);
				}
			}
    	}
	}

	private ArrayList<Course> getUserSelections(ArrayList<Course> courseList) {
		ArrayList<Course> courseListSelection = new ArrayList<>();
		while (true) {
				System.out.print("Choose the index of the courses you want to enroll in or enter q to return to main menu: ");
				String selectedCourseIndex = TerminalManager.getInstance().read();

				if(selectedCourseIndex.length() == 1 && selectedCourseIndex.equals("q")){
					navigateToMenu();
				}

				String[] arraySelectedCourseIndicesString = selectedCourseIndex.split("[,\\s.]+");
				int[] arraySelectedCourseIndex = new int[arraySelectedCourseIndicesString.length];

				Set<Integer> selectedIndicesSet = new HashSet<>();

				try {
					if (arraySelectedCourseIndex.length < 1) {
						throw new WrongNumberOfCoursesSelectedException();
					}
					for (int i = 0; i < arraySelectedCourseIndicesString.length; i++) {
						try {
							arraySelectedCourseIndex[i] = Integer.parseInt(arraySelectedCourseIndicesString[i]);
				
							if (arraySelectedCourseIndex[i] < 1 || arraySelectedCourseIndex[i] > courseList.size()) {
								throw new UnexpectedInputException();
							}
							if (!selectedIndicesSet.add(arraySelectedCourseIndex[i])) {
								// The index was already selected; treat it as a repetition
								throw new UnexpectedInputException();
							}

							courseListSelection.add(courseList.get(arraySelectedCourseIndex[i] - 1));
						} catch (NumberFormatException e) {
							// Handle the case where the element is not a valid integer
							throw new UnexpectedInputException();
						}
					}
				
					return courseListSelection;
				} catch (UnexpectedInputException | WrongNumberOfCoursesSelectedException exception) {
					// Clear arraySelectedCourseIndicesString and courseListSelection and reuse them
					Arrays.fill(arraySelectedCourseIndicesString, null);
					courseListSelection.clear();
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