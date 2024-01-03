from features.main_menu.MenuView import MenuView
from core.general_providers.SessionController import SessionController
# UNCOMMENT: from core.repositories.NotificationRepositories import NotificationRepositories
# UNCOMMENT: from core.enums.Menu import Menu
from core.enums.UserType import UserType
from core.exceptions.UnexpectedInputException import UnexpectedInputException

class MenuController:
    def __init__(self):
        self.menu_view = MenuView()
        # UNCOMMENT: self.notification_repositories = NotificationRepositories()
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
        except UnexpectedInputException as e:
            self.menu_view.show_error_message(e)
            self.handle_menu()

        # Convert menu from string
        menu = self.convert_enum(menu_selection)
        # Navigate to module
        self.navigate_to_module(menu)

    def get_menu_items(self):
        menu_items = []
        user_type = SessionController.getInstance().get_user_type()
        # UNCOMMENT: contain_new = self.get_notification(user_type)

        # Assuming some logic to populate menu_items based on user_type and notifications
        # ...

        return menu_items

    def get_menu_selection(self):
        # Assuming some logic to get the user's menu selection
        # This could be a simple input or a more complex method depending on your UI
        return input("Please enter your menu choice: ")

    def convert_enum(self, menu_selection):
        # Convert the menu selection to a Menu enum
        # Assuming Menu is an enum with values corresponding to menu selections
        return Menu(menu_selection)

    def navigate_to_module(self, menu):
        # Logic to navigate to different modules based on the menu selection
        # This will depend on how your system is structured
        # ...

    def get_notification(self, user_type):
        # Assuming some logic to check for new notifications based on user type
        # ...
        return False

    # ... Additional methods as needed ...

# Note: This code may require adjustments to fit into your existing project structure
