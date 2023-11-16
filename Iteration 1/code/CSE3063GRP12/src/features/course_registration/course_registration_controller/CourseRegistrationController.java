package features.course_registration.course_registration_controller;

import java.util.Scanner;
import features.course_registration.course_registration_view.CourseRegistrationView;
import core.repositories.TranscriptRepository;
import core.repositories.CourseRepository;
import core.enums.ApprovalState;
import core.enums.CourseGrade;
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
		handleCourseRegistration();
	}
	
	private void handleCourseRegistration() {
		User currentStudent = SessionController.getInstance().getCurrentUser(); 
		Transcript transcript = fetchTranscript(SessionController.getInstance().getCurrentUser().getId());
		ArrayList<Course> allCoursesPerSemester = fetchCoursesBySemester(transcript.getCurrentSemester()); 
		ArrayList<Course> availableCoursesForStudent = arrangeCoursesForStudent(transcript, allCoursesPerSemester, (Student)currentStudent);
		
		CourseEnrollment courseEnrollment = courseEnrollmentRepository.getCourseEnrollmentByStudentId(currentStudent.getId());
		
		// if a student has a course enrollment, then display his/her selected course list and his/her approval status
		if(courseEnrollment != null) {
			courseRegistrationView.showCourseList(courseEnrollment.getSelectedCourseList(), courseEnrollment.getApprovalState());
			getUserInput();	
		}
		else {
			courseRegistrationView.showCourseList(availableCoursesForStudent);
			//get the courses selected by the student and send it to approval
			try{
				sendCoursesToApproval(getUserSelections(availableCoursesForStudent));|
				courseRegistrationView.showSuccessMessage();
                navigateToMenu();
			}
			catch(Exception e) {
				courseRegistrationView.showErrorMessage(e);
			}
		}
	}
	
	private Transcript fetchTranscript(String transcript) {
		return transcriptRepository.getTranscript(transcript);
	}
	
	private ArrayList<Course> fetchCoursesBySemester(int semester){
		return courseRepository.getCoursesBySemester(semester);
	}
	
	private ArrayList<Course> arrangeCoursesForStudent(Transcript transcript, ArrayList<Course> courseList, Student student){
		/* we'll check the prerequisites (an arraylist) of the courses. If the student got FF on that prerequisite, then don't show
		 * the course of this semester. remove the course from the list
		 * We shouldn't take courses from upper semesters. Then call showCourseList
		 */
        //ArrayList<Semester> semester = transcript.getListOfSemester();
        ArrayList<Semester> semester = student.getTranscript().getListOfSemester();
        Semester currentSemester = semester.get(transcript.getCurrentSemester());
        Map<Course, CourseGrade> listOfCoursesTaken = currentSemester.getListOfCoursesTaken();
        
        ArrayList<Course> availableCourses = new ArrayList<>();

        for (Course courseThisSemester : courseList) {
            if(hasPassedPrerequisite(courseThisSemester.getPrerequisite(), listOfCoursesTaken))
                availableCourses.add(courseThisSemester);
        }

        return availableCourses;
    }
	
    private boolean hasPassedPrerequisites(ArrayList<Course> prerequisites, Map<Course, CourseGrade> listOfCoursesTaken){
        
        // Iterate over the map to access individual courses and their grades
        for (Map.Entry<Course, CourseGrade> entry : listOfCoursesTaken.entrySet()) {
            Course courseTaken = entry.getKey();
            CourseGrade grade = entry.getValue();
            for(Course prerequisite: prerequisites){
                if(Objects.equals(prerequisite, courseTaken)){
                    if(grade == CourseGrade.FF || grade == CourseGrade.FD)
                        return false;
                }
            }
        }
		return true;
    }
	private ArrayList<Course> getUserSelections(ArrayList<Course> courseList){
		Scanner scanner = new Scanner(System.in);
		ArrayList<Course> courseListSelection = new ArrayList<>();
		while(true) {
			try {
				System.out.print("Choose the index of the courses you want to enroll in: ");
				String selectedCourseIndex = scanner.nextLine();
				String[] arraySelectedCourseIndicesString = selectedCourseIndex.split("[,\\s.]+");
				int[] arraySelectedCourseIndex = new int[arraySelectedCourseIndicesString.length];
				
				for(int i = 0; i < arraySelectedCourseIndicesString.length; i++){
					arraySelectedCourseIndex[i] = Integer.parseInt(arraySelectedCourseIndicesString[i]);
				}
				if(arraySelectedCourseIndex.length != 5) {
					courseListSelection.clear(); // reuse the initialized arraylist
					throw new WrongNumberOfCoursesSelectedException("You must only select 5 courses");
				}
				else {
					for(Integer index: arraySelectedCourseIndex) 
						courseListSelection.add(courseList.get(index-1));
					scanner.close();
					return courseListSelection;
				}
			} 
			catch(WrongNumberOfCoursesSelectedException exception){
				courseRegistrationView.showErrorMessage(exception);
			} 
		}
	}
	
	private void getUserInput(){
			System.out.println("Press q to return to the menu");
			String input = TerminalManager.getInstance().read();
			if(input.length() == 1 && input.equals("q")) 
				navigateToMenu();
	}

	
	private void sendCoursesToApproval(CourseEnrollment courseEnrollment) {
		/* Sends courseList to CourseEnrollmentRepository */
		User currentStudent = SessionController.getInstance().getCurrentUser(); 
		courseEnrollmentRepository.updateEnrollment((Student) currentStudent, courseEnrollmentRepository.getCourseEnrollmentByStudentId(currentStudent.getId()));
	}
	
	
	// Go back to Menu by calling MenuController
	private void navigateToMenu() {
		MenuController();
    }
}