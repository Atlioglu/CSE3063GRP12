from features.main_menu.MenuView import MenuView
from core.general_providers.SessionController import SessionController
from core.repositories.NotificationRepository import NotificationRepository
from core.enums.StudentMenu import StudentMenu
from core.enums.AdvisorMenu import AdvisorMenu
from core.enums.UserType import UserType
from core.exceptions.UnexpectedInputException import UnexpectedInputException

class MenuController:
    def __init__(self):
        self.menu_view = MenuView()
        self.notification_repository = NotificationRepository()
        self.handle_menu()

    def handle_menu(self):
        # Get menu items
        menu_items = self.get_menu_items()
        # Show menu list
        self.menu_view.show_menu_list(menu_items)
        # Get menu selection
        menu_selection = None

        try:
            menu_selection = self.get_menu_selection()
            if int(menu_selection) < 1 or int(menu_selection) > len(menu_items):
                raise UnexpectedInputException()
            # Convert menu from string
            menu = self.convert_enum(menu_selection)
            # Navigate to module
            self.navigate_to_module(menu)
        except UnexpectedInputException as e:
            self.menu_view.show_error_message(e)
            self.handle_menu()



    def get_menu_items(self):
        menuItems = []
        userType = SessionController.getInstance().get_user_type()

        containNew = self.get_notification(userType)
        if userType == UserType.Student:
            for item in StudentMenu:
                if item.is_notification() and containNew:
                    menuItems.append(f"*{item.get_item_message()}*")
                else:
                    menuItems.append(item.get_item_message())
        elif userType == UserType.Advisor:
            for item in AdvisorMenu:
                if item.is_notification() and containNew:
                    menuItems.append(f"*{item.get_item_message()}*")
                else:
                    menuItems.append(item.get_item_message())

        return menuItems  

    def get_notification(self, userType):
        user = SessionController.getInstance().get_current_user()
        notificationResponse = None
        try: # bu try hata veriyor AttributeError('STUDENT')
            if userType == UserType.Student or userType == UserType.Advisor:
                notificationResponse = self.notification_repository.get_notification(user.userName) #sorun burda

            if notificationResponse is None:
                return False
            else:
                return notificationResponse.containsNew
        except Exception as e:
            print(e)

        return True

    def navigate_to_module(self, menu):
        menu.navigate()

    def get_menu_selection(self):
        selection = input("Please enter your selection: ")

        if not selection.isdigit():
            raise UnexpectedInputException()
        return selection
    
    def convert_enum(self, menu_selection):
        menu = None
        user_type = SessionController.getInstance().get_user_type()


        if user_type == UserType.Student:
            menu_list = list(StudentMenu)
            menu = menu_list[int(menu_selection)-1]
        elif user_type == UserType.Advisor:
            menu_list = list(AdvisorMenu)
            menu = menu_list[int(menu_selection)-1]

        return menu
    

