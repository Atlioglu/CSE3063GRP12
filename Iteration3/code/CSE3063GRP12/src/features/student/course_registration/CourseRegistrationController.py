from features.student.course_registration.CourseRegistrationView import CourseRegistrationView
from core.repositories.TranscriptRepository import TranscriptRepository
from core.repositories.CourseRepository import CourseRepository
from core.repositories.CourseEnrollmentRepository import CourseEnrollmentRepository
from core.repositories.NotificationRepository import NotificationRepository
from core.general_providers.SessionController import SessionController
from core.enums.ApprovalState import ApprovalState
from core.exceptions.UnexpectedInputException import UnexpectedInputException
from core.exceptions.UserNotFoundException import UserNotFoundException
from core.enums.CourseGrade import CourseGrade



class CourseRegistrationController:

    def __init__(self):
        self.courseRegistrationView = CourseRegistrationView()
        self.transcriptRepository = TranscriptRepository()
        self.courseRepository = CourseRepository()
        self.courseEnrollmentRepository = CourseEnrollmentRepository()
        self.notificationRepositories = NotificationRepository()
        self.handle_course_registration()

    def handle_course_registration(self):
        try:
            current_student = SessionController.getInstance().get_current_user()
            course_enrollment = self.courseEnrollmentRepository.get_course_enrollment_by_student_id(current_student.userName)

            if course_enrollment is not None and (course_enrollment.approvalState == ApprovalState.APPROVED or course_enrollment.approvalState == "Approved"):
                # Handle existing CourseEnrollment
                self.handle_accepted_course_enrollment(course_enrollment)
            elif course_enrollment is not None and (course_enrollment.approvalState == ApprovalState.PENDING or course_enrollment.approvalState == "Pending"):
                self.handle_pending_course_enrollment(course_enrollment)
            elif course_enrollment is not None and (course_enrollment.approvalState == ApprovalState.REJECTED or course_enrollment.approvalState == "Rejected"):
                self.handle_null_or_rejected_course_enrollment(course_enrollment, current_student)
            elif course_enrollment is None:
                self.handle_null_or_rejected_course_enrollment(None, current_student)
        except Exception as e:    
            self.courseRegistrationView.show_error_message(e)
    
    def handle_pending_course_enrollment(self, course_enrollment):
        pending_courses = self.get_pending_courses(course_enrollment.selectedCourseList, course_enrollment)
        # Needs optimization
        current_student = SessionController.getInstance().get_current_user()
        try:
            transcript = self.fetch_transcript(current_student.userName)
            all_courses_per_semester = self.fetch_courses_by_semester(transcript.currentSemester)
            available_courses_for_student = self.arrange_courses_for_student(transcript, all_courses_per_semester)
            available_courses_for_student.extend(self.get_retake_courses(transcript))

            # Add the courses that are not selected and show them
            filter_out_course_list = []
            filter_out_course_list.extend(self.get_courses_not_selected_by_student(course_enrollment, available_courses_for_student))
            self.courseRegistrationView.show_course_list(pending_courses, course_enrollment.approvalState)

            if len(filter_out_course_list) > 0:
                user_input = input("Do you want to add courses? (yes/no): ").lower()

                if user_input == "yes":
                    self.courseRegistrationView.show_course_list(filter_out_course_list)

                    # Get the courses selected by the student
                    current_selected_courses = course_enrollment.selectedCourseList
                    new_course_list_selection = self.get_user_selections(filter_out_course_list)

                    # Add/drop courses before sending them to the advisor
                    new_course_list_selection = self.add_drop_courses_options(new_course_list_selection, available_courses_for_student)

                    if new_course_list_selection and len(new_course_list_selection) > 0:
                        # Set the selected courses in the course enrollment
                        current_selected_courses.extend(new_course_list_selection)
                        course_enrollment.selectedCourseList = current_selected_courses

                        # Send the updated course enrollment to the advisor
                        self.send_notification("uploaded")

                        self.send_courses_to_approval(course_enrollment, new_course_list_selection, ApprovalState.PENDING)
                        self.courseRegistrationView.show_success_message()
                elif user_input == "no":
                    pass
                else:
                    raise UnexpectedInputException()
            self.get_user_input()
        except (IOError, UserNotFoundException, UnexpectedInputException) as e:
            self.courseRegistrationView.show_error_message(e)
            self.handle_pending_course_enrollment(course_enrollment)

    def get_pending_courses(self, selected_courses, course_enrollment):
        pending_courses = []

        if course_enrollment.approved_course_list is None and course_enrollment.rejected_course_list is None:
            # No approved or rejected courses, all courses are pending
            return selected_courses

        for course in selected_courses:
            # Check if the course is not in the approved or rejected lists
            if not self.contains_course_with_id(course_enrollment.approved_course_list, course) and \
                    not self.contains_course_with_id(course_enrollment.rejected_course_list, course):
                pending_courses.append(course)

        return pending_courses   

    def handle_accepted_course_enrollment(self, course_enrollment):
        self.courseRegistrationView.show_course_list(course_enrollment.approved_course_list, ApprovalState.APPROVED)
        self.get_user_input() 
    
    def handle_null_or_rejected_course_enrollment(self, course_enrollment, current_student):
        try:
            transcript = self.fetch_transcript(current_student.userName)
            all_courses_per_semester = self.fetch_courses_by_semester(transcript.currentSemester)
            available_courses_for_student = self.arrange_courses_for_student(transcript, all_courses_per_semester)

            all_courses = []

            if course_enrollment is not None and (course_enrollment.approvalState == ApprovalState.REJECTED or course_enrollment.approvalState == "Rejected"):
                self.handle_rejected_enrollment(transcript, course_enrollment, available_courses_for_student, all_courses)
            else:
                self.handle_new_enrollment(transcript, course_enrollment, all_courses, available_courses_for_student)

            self.navigate_to_menu()
        except (IOError, UserNotFoundException) as e:
            self.courseRegistrationView.show_error_message(e)
    
    def handle_rejected_enrollment(self, transcript, course_enrollment, available_courses_for_student, all_courses):
        available_courses_for_student.extend(self.get_retake_courses(transcript))
        # Add the courses that are not selected
        all_courses.extend(self.get_courses_not_selected_by_student(course_enrollment, available_courses_for_student))

        # Show approved courses if any
        self.show_approved_courses(course_enrollment)

        # Show rejected courses if any
        self.show_rejected_courses(course_enrollment)

        if len(all_courses) > 0:
            while True:
                if course_enrollment.approved_course_list is None or len(course_enrollment.approved_course_list) == 0:
                    print("You don't have any approved course. You need to select courses again.")
                    self.show_available_courses(course_enrollment, all_courses)
                    break
                print("Option-1: Do you want to finalize your enrollment with the approved courses? -or- Option-2: Do you want to choose other courses? ")
                user_input = input("Choose 1 or 2: ")
                try:
                    if user_input == "1":
                        self.send_courses_to_approval(course_enrollment, course_enrollment.approved_course_list, ApprovalState.APPROVED)
                    elif user_input == "2":
                        # Show available courses for selection
                        self.show_available_courses(course_enrollment, all_courses)
                        break
                    else:
                        raise UnexpectedInputException()
                except UnexpectedInputException as e:
                    self.courseRegistrationView.show_error_message(e)
        elif len(all_courses) == 0:
            self.send_courses_to_approval(course_enrollment, course_enrollment.approved_course_list, ApprovalState.APPROVED)
        self.navigate_to_menu()
    
    def update_transcript(self, course_enrollment):
        self.transcriptRepository.update_transcript(course_enrollment)

    def update_current_quota(self, course_enrollment):
        try:
            self.courseRepository.updateCurrentQuota(course_enrollment)
        except Exception as e:
            print(e)

    def handle_new_enrollment(self, transcript, course_enrollment, all_courses, available_courses_in_current_semester):
        all_courses.extend(available_courses_in_current_semester)
        all_courses.extend(self.get_retake_courses(transcript))
        self.courseRegistrationView.show_course_list(all_courses)

        # Get the courses selected by the student
        new_course_list_selection = self.get_user_selections(all_courses)

        # Add/drop courses before sending them to the advisor
        new_course_list_selection = self.add_drop_courses_options(new_course_list_selection, all_courses)

        if new_course_list_selection and len(new_course_list_selection) > 0:
            # Send the combined list to approval
            self.send_notification("registered")
            self.send_courses_to_approval(course_enrollment, new_course_list_selection, ApprovalState.PENDING)
            self.courseRegistrationView.show_success_message()
    
    def check_quota(self, course):
        try:
            # Create a CourseRepository object
            course_repository = CourseRepository()

            # Get the quota and currentQuota for the selected course from the CourseRepository
            quota = course_repository.getQuota(course.courseCode)
            current_quota = course_repository.getCurrentQuota(course.courseCode)

            # Check if the currentQuota is greater than or equal to the quota
            if current_quota >= quota:
                # If the quota is full, return false
                return False
            return True
        except IOError:
            return False
    
    def show_approved_courses(self, course_enrollment):
        if course_enrollment.approved_course_list is not None and len(course_enrollment.approved_course_list) > 0:
            print("Your approved courses: ")
            self.courseRegistrationView.show_course_list(course_enrollment.approved_course_list)
    
    def show_rejected_courses(self, course_enrollment):
        if course_enrollment.rejected_course_list is not None and len(course_enrollment.rejected_course_list) > 0:
            print("Your rejected courses: ")
            self.courseRegistrationView.show_course_list(course_enrollment.rejected_course_list)

    def show_available_courses(self, course_enrollment, all_courses):
        if len(all_courses) > 0:
            print("Available courses for selection: ")
            self.courseRegistrationView.show_course_list(all_courses)
            self.handle_course_selection(course_enrollment, all_courses)
        else:
            print("You don't have any course remaining to select from. Your registration is finalized")
            if course_enrollment is not None:
                course_enrollment.approvalState = ApprovalState.APPROVED
                self.courseRegistrationView.show_course_list(course_enrollment.approved_course_list, ApprovalState.APPROVED)       

    def handle_course_selection(self, course_enrollment, all_courses):
        # Get the courses selected by the student
        new_course_list_selection = self.get_user_selections(all_courses)
        reserve_courses = []

        # Add/drop courses before sending them to the advisor
        new_course_list_selection = self.add_drop_courses_options(new_course_list_selection, all_courses)

        if new_course_list_selection and len(new_course_list_selection) > 0:
            # Check the quota for each course in the new selection
            for selected_course in new_course_list_selection:
                if not self.check_quota(selected_course):
                    print(f"Sorry, the quota for the course {selected_course.name} is full. Please choose another course.")
                    self.navigate_to_menu()
                    return  # Stop further processing

            # Reserve the previously approved courses to not be lost
            reserve_courses = self.reserve_previously_approved_courses(course_enrollment, new_course_list_selection)

            # Set the selected courses in the course enrollment
            if course_enrollment is not None:
                course_enrollment.selectedCourseList = reserve_courses
            self.send_notification("uploaded")
            self.send_courses_to_approval(course_enrollment, reserve_courses, ApprovalState.PENDING)

            self.courseRegistrationView.show_success_message()

        self.navigate_to_menu()
    
    def reserve_previously_approved_courses(self, course_enrollment, new_course_list_selection):
        reserve_courses = []
        if course_enrollment is not None:
            reserve_courses.extend(course_enrollment.selectedCourseList)
            reserve_courses.extend(new_course_list_selection)
        return reserve_courses
    
    def get_courses_not_selected_by_student(self, course_enrollment, all_courses):
        not_selected_courses = []
        
        for course in all_courses:
            if not self.contains_course_with_id(course_enrollment.selectedCourseList, course):
                not_selected_courses.append(course)
        return not_selected_courses
    
    def contains_course_with_id(self, courses, target_course):
        for course in courses:
            if isinstance(target_course, dict):
                if course.get("id") == target_course.get("id"):
                    return True
            else:
                if course.get("id") == target_course.id:
                    return True
        return False

    def get_retake_courses(self, transcript):
        retake_course_ids = self.find_retake_course_ids(transcript)
        return self.courseRepository.findCoursesWithCourseIds(retake_course_ids)
    
    def find_retake_course_ids(self, transcript):
        retake_course_ids = []

        for semester in transcript.listOfSemesters.values():
            for course_id, grade in semester["listOfCoursesTaken"].items():
                # Check if the grade is DD, DC, or FF, and add the courseId to retakeCourseIds
                if grade in [CourseGrade.DD, CourseGrade.DC, CourseGrade.FF]:
                    retake_course_ids.append(course_id)

        return retake_course_ids
    
    def fetch_transcript(self, student_id):
        return self.transcriptRepository.get_transcript(student_id)
    
    def fetch_courses_by_semester(self, semester):
        return self.courseRepository.get_courses_by_semester(semester)
    
    def arrange_courses_for_student(self, transcript, current_semester_course_list):
        semesters = transcript.listOfSemesters
        available_courses = []

        for course_this_semester in current_semester_course_list:
            if not semesters or not semesters.values() or len(semesters.values()) == 0:
                available_courses.append(course_this_semester)
            else:
                # Check if the course has prerequisites, and if the student has passed them
                if self.has_passed_prerequisites(course_this_semester.prerequisites, semesters):
                    available_courses.append(course_this_semester)

        return available_courses
    
    def has_passed_prerequisites(self, prerequisites, semesters):
        for prerequisite in prerequisites:
            for semester in semesters.values():
                grade = semester.listOfCoursesTaken.get(prerequisite.courseCode)
                if grade in [CourseGrade.FF, CourseGrade.FD]:
                    return False  # Student has failed the prerequisite
        return True  # Student has passed all prerequisites
    
    def add_drop_courses_options(self, course_list, available_courses):
        valid_input = False
        while not valid_input:
            try:
                decision = input("Do you want to add or drop any courses? (type 'add', 'drop', or 'done') ").lower()

                if decision == "add":
                    self.add_course_option(course_list, available_courses)
                elif decision == "drop":
                    self.drop_course_option(course_list)
                elif decision == "done":
                    valid_input = True
                else:
                    raise UnexpectedInputException()

                # Check if any selected course has a full quota
                if self.has_course_with_full_quota(course_list):
                    print("Sorry, the quota for at least one of the selected courses is full. Please try again.")
                    course_list.clear()  # Clear the list to prevent further processing
                    continue  # Reprompt user

                # Additional check for sending enrollment
                if len(course_list) > 0:
                    
                    # Check if any selected course has a full quota
                    if self.has_course_with_full_quota(course_list):
                        print("Sorry, the quota for at least one of the selected courses is full. Please try again.")
                        course_list.clear()  # Clear the list to prevent further processing
                        continue  # Reprompt user
                    
                    decision = input("Do you want to send your enrollment to your advisor? (yes/no) ").lower()
                    if decision == "yes":
                        # Check again before sending to advisor to avoid sending courses with full quota
                        if not self.has_course_with_full_quota(course_list):
                            valid_input = True
                        else:
                            print("Sorry, the quota for at least one of the selected courses is full. Please try again.")
                            course_list.clear()  # Clear the list to prevent further processing
                    elif decision == "no":
                        return None
                    else:
                        raise UnexpectedInputException()
            except UnexpectedInputException as e:
                self.courseRegistrationView.show_error_message(e)
                    

        return course_list
    
    def has_course_with_full_quota(self, course_list):
        for course in course_list:
            if not self.check_quota(course):
                return True
        return False
    
    def add_course_option(self, course_list, available_courses):
        if len(course_list) == len(available_courses):
            print("You have selected all courses!")
            return

        # Create a new list to hold courses that are not in courseList
        courses_to_display = [course for course in available_courses if course not in course_list]
        self.courseRegistrationView.show_course_list(courses_to_display)

        # Get the courses selected by the student, concatenate all courses selected and send it to approval
        new_course_list_selection = self.get_user_selections(courses_to_display)
        course_list.extend(new_course_list_selection)

    def drop_course_option(self, course_list):
        if len(course_list) == 0:
            print("You don't have any courses to drop!")
            return

        is_valid = False
        while not is_valid:
            self.courseRegistrationView.show_course_list(course_list)
            selection = input("Which courses do you want to drop? ")
            selection_array = selection.split(' ')

            # Use a set to prevent repetition of inputs
            indices_to_remove = set()

            try:
                for selection_index in selection_array:
                    try:
                        index = int(selection_index)
                        if 0 < index <= len(course_list):
                            indices_to_remove.add(index - 1)
                        else:
                            raise UnexpectedInputException()
                    except ValueError:
                        raise UnexpectedInputException()

                # Check for repetitions
                if len(indices_to_remove) != len(selection_array):
                    raise UnexpectedInputException()

                is_valid = True

            except UnexpectedInputException as exception:
                self.courseRegistrationView.show_error_message(exception)

            # Remove the selected courses only if input is valid
            if is_valid:
                indices_to_remove_list = sorted(list(indices_to_remove), reverse=True)

                for index_to_remove in indices_to_remove_list:
                    course_list.pop(index_to_remove)

    def get_user_selections(self, course_list):
        new_course_list_selection = []
        while True:
            skip_to_while_loop = False
            selected_course_index = input(
                "Choose the index of the courses you want to enroll in or enter q to return to the main menu: ")

            if len(selected_course_index) == 1 and selected_course_index == "q":
                self.navigate_to_menu()

            array_selected_course_indices_string = selected_course_index.split(' ')

            try:
                if len(array_selected_course_indices_string) < 1:
                    raise UnexpectedInputException()

                selected_indices_set = set()
                array_selected_course_index = [0] * len(array_selected_course_indices_string)
                for i in range(len(array_selected_course_indices_string)):
                    try:
                        array_selected_course_index[i] = int(array_selected_course_indices_string[i])

                        if array_selected_course_index[i] < 1 or array_selected_course_index[i] > len(course_list):
                            raise UnexpectedInputException()
                        length = len(selected_indices_set)
                        selected_indices_set.add(array_selected_course_index[i])
                        if len(selected_indices_set) == length:
                            # The index was already selected; treat it as a repetition
                            raise UnexpectedInputException()

                        selected_course = course_list[array_selected_course_index[i] - 1]

                        # Check if the quota for the selected course is full
                        if not self.check_quota(selected_course):
                            print(f"Sorry, the quota for the course {selected_course.name} is full. Please choose another course.")
                            new_course_list_selection.clear()  # Clear the list to prevent further processing
                            skip_to_while_loop = True
                            continue  # Reprompt user

                        new_course_list_selection.append(selected_course)

                    except ValueError:
                        # Handle the case where the element is not a valid integer
                        raise UnexpectedInputException()

                if skip_to_while_loop or self.has_course_with_full_quota(new_course_list_selection):
                    # Clear new_course_list_selection and reuse them
                    new_course_list_selection.clear()
                    continue

                return new_course_list_selection

            except (UnexpectedInputException) as exception:
                # Clear new_course_list_selection and reuse them
                new_course_list_selection.clear()
                self.courseRegistrationView.show_error_message(exception)
                
    def get_user_input(self):
        print("Press q to return to the menu")
        while True:
            try:
                input_value = input()
                if len(input_value) == 1 and input_value == "q":
                    self.navigate_to_menu()
                    break
                else:
                    raise UnexpectedInputException()
            except UnexpectedInputException as e:
                self.courseRegistrationView.show_error_message(e)
    
    def send_courses_to_approval(self, course_enrollment, course_list, approval_state):
        try:
            if course_enrollment is None:
                self.courseEnrollmentRepository.create_course_enrollment(course_list)
            else:
                self.courseEnrollmentRepository.update_enrollment(course_enrollment, 
                                                                  course_enrollment.studentId, 
                                                                  course_enrollment.approved_course_list,
                                                                  course_enrollment.rejected_course_list,
                                                                  approval_state)
                
                if approval_state == ApprovalState.APPROVED:
                    self.update_transcript(course_enrollment)
                    self.update_current_quota(course_enrollment)
                self.courseRegistrationView.show_success_message()
                self.navigate_to_menu()
        except Exception as e:
            self.courseRegistrationView.show_error_message(e)
    
    def navigate_to_menu(self):
        from features.main_menu.MenuController import MenuController
        MenuController()

    def send_notification(self, message):
        current_student = SessionController.getInstance().get_current_user()
        self.notificationRepositories.update_notification(current_student.advisor["userName"], current_student.userName + " has " + message)




    



