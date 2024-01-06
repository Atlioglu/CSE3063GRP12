from core.repositories.CourseEnrollmentRepository import CourseEnrollmentRepository
from core.exceptions.UnexpectedInputException import UnexpectedInputException
from features.advisor.approval_courses_selected.ApprovalCoursesSelectedController import ApprovalCoursesSelectedController
from features.advisor.course_approval.CourseApprovalView import CourseApprovalView


class CourseApprovalController:
    def __init__(self):
        self.__courseEnrollmentRepository = CourseEnrollmentRepository()
        self.__courseApprovalView = CourseApprovalView()

        try:
            self.handle_approval_controller()
        except UnexpectedInputException as e:
            self.__courseApprovalView.showErrorMessage(e)
            CourseApprovalController()

    def fetch_pending_enrollments(self):
        return self.__courseEnrollmentRepository.get_pending_enrollments()

    def navigate_to_approval_courses_selected(self, course_enrollment):
        ApprovalCoursesSelectedController(course_enrollment)

    def navigate_to_menu(self):
        from features.main_menu.MenuController import MenuController
        MenuController()

    def handle_approval_controller(self):
        pending_enrollments = self.fetch_pending_enrollments()
        self.__courseApprovalView.showPendingCourseEnrollments(pending_enrollments)


        selection = input("Please enter your selection: ")
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
