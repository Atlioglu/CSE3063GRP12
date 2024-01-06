from core.general_providers.SessionController import SessionController
from core.models.concretes.Advisor import Advisor
from core.repositories.UserRepository import UserRepository
from features.advisor.student_list.AdvisorStudentListView import AdvisorStudentListView

class AdvisorStudentListController:
    def __init__(self):
        self.__advisorStudentListView = AdvisorStudentListView()
        self.__userRepository = UserRepository()
        self.handle_student_list()

    def navigate_to_menu(self):
        from features.main_menu.MenuController import MenuController
        MenuController()

    def handle_student_list(self):
        advisor = SessionController.getInstance().get_current_user()
        if isinstance(advisor, Advisor):
            self.__advisorStudentListView.showStudentList(self.__userRepository.get_students_by_advisor(advisor))
            self.__advisorStudentListView.showQuitMessage()
            user_input = input()
            try:
                if user_input == "q":
                    self.navigate_to_menu()
                else:
                    self.__advisorStudentListView.showErrorMessage()
            except Exception as e:
                print("Error:", str(e))
                self.handle_student_list()
