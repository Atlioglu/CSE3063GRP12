from ....core.general_providers import TerminalManager
from ....core.general_providers import SessionController
from ....core.models.concretes import Advisor
from ....core.repositories import UserRepository
from ..main_menu import MenuController
from . import AdvisorStudentListView

class AdvisorStudentListController:
    def __init__(self):
        self.__advisorStudentListView = AdvisorStudentListView()
        self.__userRepository = UserRepository()
        self.handle_student_list()

    def navigate_to_menu(self):
        MenuController()

    def get_user_input(self):
        input_value = TerminalManager.getInstance().read()
        return input_value

    def handle_student_list(self):
        advisor = SessionController.getInstance().getCurrentUser()
        if isinstance(advisor, Advisor):
            self.__advisorStudentListView.show_student_list(self.__userRepository.get_students_by_advisor(advisor))
            self.__advisorStudentListView.show_quit_message()
            user_input = self.get_user_input()
            try:
                if user_input == "q":
                    self.navigate_to_menu()
                else:
                    self.__advisorStudentListView.show_error_message()
            except Exception as e:
                print("Error:", str(e))
                self.handle_student_list()
