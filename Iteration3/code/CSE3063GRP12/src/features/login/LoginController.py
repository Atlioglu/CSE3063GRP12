from features.login.LoginView import LoginView
from core.repositories.UserRepository import UserRepository
from core.exceptions.UserNotFoundException import UserNotFoundException
from core.exceptions.WrongPasswordException import WrongPasswordException
from features.main_menu.MenuController import MenuController

class LoginController:

    def __init__(self):
        self.login_view = LoginView()
        self.user_repository = UserRepository()
        self.handle_login()

    def navigate_to_menu(self):
        MenuController()

    def get_username_input(self):
        return input("Please enter your username: ")

    def get_password_input(self):
        return input("Please enter your password: ")

    def handle_login(self):
        login_successful = False
        while not login_successful:
            try:
                username = self.get_username_input()
                password = self.get_password_input()

                self.user_repository.login_check(username, password)
                self.navigate_to_menu()
                login_successful = True
            except (UserNotFoundException, WrongPasswordException) as e:
                self.login_view.show_error(e)
            except Exception as e:
                self.login_view.show_error(e)
                break
