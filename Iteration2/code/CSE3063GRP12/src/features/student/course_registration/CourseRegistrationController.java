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
import core.models.concretes.Student;
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
			Student currentStudent = (Student) SessionController.getInstance().getCurrentUser();
			CourseEnrollment courseEnrollment = courseEnrollmentRepository
					.getCourseEnrollmentByStudentId(currentStudent.getUserName());

			if (courseEnrollment != null && courseEnrollment.getApprovalState() == ApprovalState.Approved) {
				// Handle existing CourseEnrollment
				handleAcceptedCourseEnrollment(courseEnrollment);
			}
			else if(courseEnrollment != null && courseEnrollment.getApprovalState() == ApprovalState.Pending){
				handlePendingCourseEnrollment(courseEnrollment);
			}
			else if(courseEnrollment != null && courseEnrollment.getApprovalState() == ApprovalState.Rejected)
				handleNullOrRejectedCourseEnrollment(courseEnrollment, currentStudent);
			else if(courseEnrollment == null)
					handleNullOrRejectedCourseEnrollment(null, currentStudent);
		} catch (Exception e) {
			courseRegistrationView.showErrorMessage(e);
		}
	}

	/* Prompts the user to add/drop to the pending course list */
	private void handlePendingCourseEnrollment(CourseEnrollment courseEnrollment){
		ArrayList<Course> pendingCourses = getPendingCourses(courseEnrollment.getSelectedCourseList(), courseEnrollment);
		courseRegistrationView.showCourseList(pendingCourses, courseEnrollment.getApprovalState());
		   
		try{
			System.out.print("Do you want to add/drop courses? (yes/no): ");
			String userInput = TerminalManager.getInstance().read().toLowerCase();
		
			if ("yes".equals(userInput)) {
				// needs optimization
				User currentStudent = SessionController.getInstance().getCurrentUser();
				Transcript transcript = fetchTranscript(currentStudent.getUserName());
				ArrayList<Course> allCoursesPerSemester = fetchCoursesBySemester(transcript.getCurrentSemester());
				ArrayList<Course> availableCoursesForStudent = arrangeCoursesForStudent(transcript, allCoursesPerSemester);
				availableCoursesForStudent.addAll(getRetakeCourses(transcript));
				
				// add the courses that are not selected and show them
				ArrayList<Course> filterOutCourseList = new ArrayList<>();
				filterOutCourseList.addAll(getCoursesNotSelectedByStudent(courseEnrollment, availableCoursesForStudent));

				courseRegistrationView.showCourseList(filterOutCourseList);
				
				// get the courses selected by the student
				ArrayList<Course> currentSelectedCourses = courseEnrollment.getSelectedCourseList();
				ArrayList<Course> newCourseListSelection = getUserSelections(filterOutCourseList);
		
				// add/drop courses before sending them to the advisor
				newCourseListSelection = addDropCoursesOptions(newCourseListSelection, availableCoursesForStudent);
		
				if (!newCourseListSelection.isEmpty()) {
					// set the selected courses in the course enrollment
					currentSelectedCourses.addAll(newCourseListSelection);
					courseEnrollment.setSelectedCourseList(currentSelectedCourses);
		
					// send the updated course enrollment to the advisor
					sendCoursesToApproval(courseEnrollment, newCourseListSelection, ApprovalState.Pending);
					courseRegistrationView.showSuccessMessage();
				}
			}
			else if(userInput.equals("no"));
			else throw new UnexpectedInputException();
			getUserInput();
		}
		 	catch (IOException | UserNotFoundException | UnexpectedInputException e) {
				courseRegistrationView.showErrorMessage(e);
				handlePendingCourseEnrollment(courseEnrollment);
			}			
	}

	// helper method to get pending courses from the selected course list
	private ArrayList<Course> getPendingCourses(ArrayList<Course> selectedCourses, CourseEnrollment courseEnrollment) {
		ArrayList<Course> pendingCourses = new ArrayList<>();

		if (courseEnrollment.getApprovedCourseList() == null && courseEnrollment.getRejectedCourseList() == null) {
			// no approved or rejected courses, all courses are pending
			return selectedCourses;
		}

		for (Course course : selectedCourses) {
			// check if the course is not in the approved or rejected lists
			if (!containsCourseWithId(courseEnrollment.getApprovedCourseList(), course) &&
				!containsCourseWithId(courseEnrollment.getRejectedCourseList(), course)) {
				pendingCourses.add(course);
			}
		}
		return pendingCourses;
	}

	private void handleAcceptedCourseEnrollment(CourseEnrollment courseEnrollment) {
		courseRegistrationView.showCourseList(courseEnrollment.getApprovedCourseList(),
				ApprovalState.Approved);
		getUserInput();
	}

	private void handleNullOrRejectedCourseEnrollment(CourseEnrollment courseEnrollment, User currentStudent) {
		try {
			Transcript transcript = fetchTranscript(currentStudent.getUserName());
			ArrayList<Course> allCoursesPerSemester = fetchCoursesBySemester(transcript.getCurrentSemester());
			ArrayList<Course> availableCoursesForStudent = arrangeCoursesForStudent(transcript, allCoursesPerSemester);
	
			ArrayList<Course> allCourses = new ArrayList<>();
	
			if (courseEnrollment != null && courseEnrollment.getApprovalState() == ApprovalState.Rejected) {
				handleRejectedEnrollment(transcript, courseEnrollment, availableCoursesForStudent, allCourses);
			} else {
				handleNewEnrollment(courseEnrollment, allCourses, transcript, availableCoursesForStudent);
			}
			
			handleCourseSelection(courseEnrollment, allCourses);
			navigateToMenu();
		} catch (IOException | UserNotFoundException e) {
			courseRegistrationView.showErrorMessage(e);
		}
	}
	
	private void handleRejectedEnrollment(Transcript transcript, CourseEnrollment courseEnrollment, ArrayList<Course> availableCoursesForStudent, ArrayList<Course> allCourses) {
		availableCoursesForStudent.addAll(getRetakeCourses(transcript));
		// add the courses that are not selected 
		allCourses.addAll(getCoursesNotSelectedByStudent(courseEnrollment, availableCoursesForStudent));

		// show approved courses if any
		showApprovedCourses(courseEnrollment);
	
		// show rejected courses if any
		showRejectedCourses(courseEnrollment);
	
		// show available courses for selection
		showAvailableCourses(courseEnrollment, allCourses);

        navigateToMenu();		

	}	
	
	private void handleNewEnrollment(CourseEnrollment courseEnrollment, ArrayList<Course> allCourses, Transcript transcript, ArrayList<Course> availableCoursesInCurrentSemester) {
		allCourses.addAll(availableCoursesInCurrentSemester);
		allCourses.addAll(getRetakeCourses(transcript));
		courseRegistrationView.showCourseList(allCourses);
	
		// get the courses selected by the student
		ArrayList<Course> newCourseListSelection = getUserSelections(allCourses);
	
		// add/drop courses before sending them to the advisor
		newCourseListSelection = addDropCoursesOptions(newCourseListSelection, allCourses);
	
		if (!newCourseListSelection.isEmpty()) {
			// send the combined list to approval
			sendCoursesToApproval(courseEnrollment, newCourseListSelection, ApprovalState.Pending);
			courseRegistrationView.showSuccessMessage();
		}
	}

	private boolean checkQuota(Course course) throws IOException {
		// Create a CourseRepository object
		CourseRepository courseRepository = new CourseRepository();

		// Get the quota and currentQuota for the selected course from the CourseRepository
		int quota = courseRepository.getQuota(course.getCourseCode());
		int currentQuota = courseRepository.getCurrentQuota(course.getCourseCode());

		// Check if the currentQuota is greater than or equal to the quota
		if (currentQuota >= quota) {
			// If the quota is full, print a message and return false
			return false;
		}
		return true;
	}

	private void showApprovedCourses(CourseEnrollment courseEnrollment) {
		if (courseEnrollment.getApprovedCourseList().size() > 0) {
			System.out.println("Your approved courses: ");
			courseRegistrationView.showCourseList(courseEnrollment.getApprovedCourseList());
			System.out.println();
		}
	}
	
	private void showRejectedCourses(CourseEnrollment courseEnrollment) {
		if (courseEnrollment.getRejectedCourseList().size() > 0) {
			System.out.println("Your rejected courses: ");
			courseRegistrationView.showCourseList(courseEnrollment.getRejectedCourseList());
			System.out.println();
		}
	}
	
	private void showAvailableCourses(CourseEnrollment courseEnrollment, ArrayList<Course> allCourses) {
		if (allCourses.size() > 0) {
			System.out.println("Available courses for selection: ");
			courseRegistrationView.showCourseList(allCourses);
			handleCourseSelection(courseEnrollment, allCourses);
		} else {
			System.out.println("You don't have any course remaining to select from. Your registration is finalized");
			if (courseEnrollment != null) {
				courseEnrollment.setApprovalState(ApprovalState.Approved);
				courseRegistrationView.showCourseList(courseEnrollment.getApprovedCourseList(), ApprovalState.Approved);
			}
		}
	}

	private void handleCourseSelection(CourseEnrollment courseEnrollment, ArrayList<Course> allCourses) {
		// get the courses selected by the student
		ArrayList<Course> newCourseListSelection = getUserSelections(allCourses);
		ArrayList<Course> reserveCourses = new ArrayList<>();
	
		// add/drop courses before sending them to the advisor
		newCourseListSelection = addDropCoursesOptions(newCourseListSelection, allCourses);
	
		if (!newCourseListSelection.isEmpty()) {
			// reserve the previously approved courses to not be lost
			reserveCourses = reservePreviouslyApprovedCourses(courseEnrollment, newCourseListSelection);
	
			// set the selected courses in the course enrollment
			if (courseEnrollment != null) {
				courseEnrollment.setSelectedCourseList(reserveCourses);
			}
			sendCoursesToApproval(courseEnrollment, reserveCourses, ApprovalState.Pending);
	
			courseRegistrationView.showSuccessMessage();
		}
		navigateToMenu();
	}
	
	private ArrayList<Course> reservePreviouslyApprovedCourses(CourseEnrollment courseEnrollment, ArrayList<Course> newCourseListSelection) {
		ArrayList<Course> reserveCourses = new ArrayList<>();
		if (courseEnrollment != null) {
			reserveCourses.addAll(courseEnrollment.getSelectedCourseList());
			reserveCourses.addAll(newCourseListSelection);
		}
		return reserveCourses;
	}

	// Helper method to get courses that are not selected by the student
	private ArrayList<Course> getCoursesNotSelectedByStudent(CourseEnrollment courseEnrollment, ArrayList<Course> allCourses) {
		ArrayList<Course> notSelectedCourses = new ArrayList<>();
	
		for (Course course : allCourses) {
			if (!containsCourseWithId(courseEnrollment.getSelectedCourseList(), course)) {
				notSelectedCourses.add(course);
			}
		}
		return notSelectedCourses;
	}
	
	// Helper method to check if a list contains a course with a specific id
	private boolean containsCourseWithId(ArrayList<Course> courses, Course targetCourse) {
		for (Course course : courses) {
			// Use a unique identifier to compare courses
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
				// if a course doesn't have a prerequisite, it will be added to the list
				if (hasPassedPrerequisites(courseThisSemester.getPrerequisites(), semesters)) {
					availableCourses.add(courseThisSemester);
				}
			}
		}	
		return availableCourses;
	}
	
	private boolean hasPassedPrerequisites(ArrayList<Course> prerequisites, Map<Integer, Semester> semesters) {
		for (Course prerequisite : prerequisites) {
			for (Semester semester : semesters.values()) {
				CourseGrade grade = semester.getListOfCoursesTaken().get(prerequisite.getCourseCode());
				if (grade == CourseGrade.FF || grade == CourseGrade.FD) {
					return false; // student has failed the prerequisite
				}
			}
		}
		return true; // student has passed all prerequisites
	}

	private ArrayList<Course> addDropCoursesOptions(ArrayList<Course> courseList, ArrayList<Course> availableCourses) {
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
					}
					else if(decision.equals("no")) continue; 
					else throw new UnexpectedInputException();
				}
			} catch (UnexpectedInputException exception) {
				courseRegistrationView.showErrorMessage(exception);
				continue; // reprompt user
			}
		}
		return courseList;
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
			ArrayList<Course> newCourseListSelection = getUserSelections(coursesToDisplay);
			courseList.addAll(newCourseListSelection);
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
			
			// used set to prevent repetition of inputs
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
		ArrayList<Course> newCourseListSelection = new ArrayList<>();
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

							newCourseListSelection.add(courseList.get(arraySelectedCourseIndex[i] - 1));
						} catch (NumberFormatException e) {
							// Handle the case where the element is not a valid integer
							throw new UnexpectedInputException();
						}
					}
				
					return newCourseListSelection;
				} catch (UnexpectedInputException | WrongNumberOfCoursesSelectedException exception) {
					// Clear arraySelectedCourseIndicesString and newCourseListSelection and reuse them
					Arrays.fill(arraySelectedCourseIndicesString, null);
					newCourseListSelection.clear();
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

	private void sendCoursesToApproval(CourseEnrollment courseEnrollment, ArrayList<Course> selectedCourses, ApprovalState approvalState) {
		try {
			if (courseEnrollment == null) {
				// Create a new CourseEnrollment if it doesn't exist
				courseEnrollmentRepository.createCourseEnrollment(selectedCourses);
			} else {
				//courseEnrollment.setSelectedCourseList(selectedCourses);
				courseEnrollmentRepository.updateEnrollment(courseEnrollment, courseEnrollment.getStudentId(),
					courseEnrollment.getApprovedCourseList(), 
					courseEnrollment.getRejectedCourseList(),
					approvalState);
			}
	
			courseRegistrationView.showSuccessMessage();
			navigateToMenu();
		} catch (Exception e) {
			courseRegistrationView.showErrorMessage(e);
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