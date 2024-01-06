import re

from features.advisor.approval_courses_selected.ApprovalCoursesSelectedView import ApprovalCoursesSelectedView
from core.repositories.CourseEnrollmentRepository import CourseEnrollmentRepository
from core.repositories.TranscriptRepository import TranscriptRepository
from core.repositories.CourseRepository import CourseRepository
from core.repositories.NotificationRepository import NotificationRepository
from core.enums.ApprovalState import ApprovalState
from core.exceptions.UnexpectedInputException import UnexpectedInputException

class ApprovalCoursesSelectedController:
    def __init__(self, current_course_enrollment):
        self.current_course_enrollment = current_course_enrollment
        self.approval_courses_selected_view = ApprovalCoursesSelectedView()
        self.course_enrollment_repository = CourseEnrollmentRepository()
        self.transcript_repository = TranscriptRepository()
        self.course_repository = CourseRepository()
        self.notification_repositories = NotificationRepository()

        self.handle_course_approval()

    def handle_course_approval(self):
        try:
            # Filter out courses that are already approved
            courses_to_review = []
            for course in self.current_course_enrollment.selectedCourseList:
                if (not self.is_course_in_list(course, self.current_course_enrollment.approved_course_list) and
                        not self.is_course_in_list(course, self.current_course_enrollment.rejected_course_list)):
                    courses_to_review.append(course)

            # Show the filtered courses to the advisor
            self.approval_courses_selected_view.show_selected_courses(courses_to_review)

            all_courses = self.get_advisor_course_selections(courses_to_review)
            approved_courses = all_courses[0]
            rejected_courses = all_courses[1]

            if len(approved_courses) == len(courses_to_review):
                # all approved, first enrollment
                if self.current_course_enrollment.approved_course_list is None:
                    self.course_enrollment_repository.update_enrollment(self.current_course_enrollment,
                                                                       self.current_course_enrollment.studentId,
                                                                       approved_courses,
                                                                       rejected_courses,
                                                                       ApprovalState.APPROVED)
                else:
                    self.current_course_enrollment.approved_course_list.extend(approved_courses)
                    self.course_enrollment_repository.update_enrollment(self.current_course_enrollment,
                                                                       self.current_course_enrollment.studentId,
                                                                       self.current_course_enrollment.approved_course_list,
                                                                       self.current_course_enrollment.rejected_course_list,
                                                                       ApprovalState.APPROVED)
                self.update_transcript(self.current_course_enrollment)
                self.update_current_quota(self.current_course_enrollment)
                self.send_notification("accepted")

            else:
                # some approved some rejected and has previous approved/rejected courses
                if (self.current_course_enrollment.approved_course_list is not None and
                        self.current_course_enrollment.rejected_course_list is not None):
                    self.current_course_enrollment.approved_course_list.extend(approved_courses)
                    self.current_course_enrollment.rejected_course_list.extend(rejected_courses)
                    self.course_enrollment_repository.update_enrollment(self.current_course_enrollment,
                                                                       self.current_course_enrollment.studentId,
                                                                       self.current_course_enrollment.approved_course_list,
                                                                       self.current_course_enrollment.rejected_course_list,
                                                                       ApprovalState.REJECTED)
                    self.send_notification("rejected, please check your course registration module!")
                else:
                    # some approved, some rejected, first enrollment
                    self.course_enrollment_repository.update_enrollment(self.current_course_enrollment,
                                                                       self.current_course_enrollment.studentId,
                                                                       approved_courses,
                                                                       rejected_courses,
                                                                       ApprovalState.REJECTED)
                    self.send_notification("some of your courses are rejected. Please check in course registration module")

                self.update_current_quota(self.current_course_enrollment)

            self.navigate_to_menu()

        except Exception as e:
            self.approval_courses_selected_view.show_error_message(e)
    
    def send_notification(self, message):
        self.notification_repositories.update_notification(self.current_course_enrollment.studentId,"Your request is "+ message)

    def is_course_in_list(self, course, course_list):
        if course_list is None or len(course_list) == 0:
            return False
        
        if isinstance(course, dict):
            # Accessing dictionary keys
            course_id = course.get('id')
        else:
            # Accessing attributes of Course object
            course_id = course.id

        if isinstance(course_list[0], dict):
            # Accessing dictionary keys
            return course_list is not None and any(existing_course.get('id') == course_id for existing_course in course_list)
        else:
            # Accessing attributes of Course object
            return course_list is not None and any(existing_course.id == course_id for existing_course in course_list)
    
    def get_advisor_course_selections(self,course_list):
        approved_courses = []
        rejected_courses = []

        while True:
            selected_course_indexes = input("Which courses do you want to approve? (0 for none): ")

            if len(selected_course_indexes) == 1 and selected_course_indexes == "0":
                # all rejected
                rejected_courses.extend(course_list)
                return [approved_courses, rejected_courses]

            try:
                if len(selected_course_indexes) < 1 or not re.match("^[0-9\s,]*$", selected_course_indexes):
                    # empty input or alphabetic input
                    raise UnexpectedInputException("Unexpected input")

                array_selected_course_indices = list(map(int, re.split("[,\s.]+", selected_course_indexes)))
                array_selected_course_indices.sort()
                iter_approved_courses_index = 0

                for i, course in enumerate(course_list):
                    if (iter_approved_courses_index < len(array_selected_course_indices) and
                            array_selected_course_indices[iter_approved_courses_index] == i + 1):
                        # Course is approved
                        approved_courses.append(course)
                        iter_approved_courses_index += 1
                    else:
                        # Course is rejected
                        rejected_courses.append(course)
                return [approved_courses, rejected_courses]

            except UnexpectedInputException as exception:
                print("Error:", exception)  # Replace this with your own error handling
    
    def update_current_quota(self, course_enrollment):
        try:
            self.course_repository.updateCurrentQuota(course_enrollment)
        except Exception as e:
            print(e)
    
    def update_transcript(self, course_enrollment):
        self.transcript_repository.update_transcript(course_enrollment)
    
    def navigate_to_menu(self):
        from features.main_menu.MenuController import MenuController
        MenuController()