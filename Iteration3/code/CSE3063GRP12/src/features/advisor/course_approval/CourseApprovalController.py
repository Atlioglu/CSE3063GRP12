from ....core.general_providers import TerminalManager
from ....core.repositories import CourseEnrollmentRepository
from ....core.exceptions import UnexpectedInputException
from ..approval_courses_selected import ApprovalCoursesSelectedController
from ..course_approval import CourseApprovalView
from ..main_menu import MenuController

class CourseApprovalController:
    def __init__(self):
        self.__courseEnrollmentRepository = CourseEnrollmentRepository()
        self.__courseApprovalView = CourseApprovalView()

        try:
            self.handle_approval_controller()
        except UnexpectedInputException as e:
            self.__courseApprovalView.show_error_message(e)
            CourseApprovalController()

    def fetch_pending_enrollments(self):
        return self.__courseEnrollmentRepository.get_pending_enrollments()

    def navigate_to_approval_courses_selected(self, course_enrollment):
        ApprovalCoursesSelectedController(course_enrollment)

    def navigate_to_menu(self):
        MenuController()

    def get_user_input(self):
        input_value = TerminalManager.get_instance().read()
        return input_value

    def handle_approval_controller(self):
        pending_enrollments = self.fetch_pending_enrollments()
        self.__courseApprovalView.show_pending_course_enrollments(pending_enrollments)

        self.__courseApprovalView.show_prompt_message()
        selection = self.get_user_input()
        if not selection.isdigit() and selection != "q":
            raise UnexpectedInputException()
        elif selection == "q":
            self.navigate_to_menu()
        else:
            index = int(selection)
            if index > len(self.__courseEnrollmentRepository.get_pending_enrollments()) or index < 0:
                raise UnexpectedInputException()
            else:
                self.navigate_to_approval_courses_selected(self.__courseEnrollmentRepository.get_pending_enrollments()[index - 1])
