from core.models.concretes.Notification import Notification
from core.repositories.NotificationRepository import NotificationRepository
from features.notification.NotificationView import NotificationView
from core.general_providers.SessionController import SessionController
from core.exceptions.UserNotFoundException import UserNotFoundException

class NotificationController:
    def __init__(self):
        self.notification_view = NotificationView()
        self.notification_repository = NotificationRepository()
        self.notification = Notification()
        self.handle_notification()

    def navigate_to_menu(self,message):
        if message == "q":
            from features.main_menu.MenuController import MenuController
            MenuController()
        else:
            print("Unexpected input!")
            message = input("Press q to return Main Menu: ")
            self.navigate_to_menu(message)



    def handle_notification(self):
        user = SessionController.getInstance().get_current_user()
        try:
            notification_response = self.notification_repository.get_notification(user.userName)
            if notification_response is None:
                print("You have no notification")
            else:
                notifications = notification_response.listOfNotification
                self.notification_view.show_notification_list(notifications)
                self.notification_repository.update_notification_read(user.userName)
                self.notification_repository.update_notification_response_read(user.userName, False)

        except (IOError, UserNotFoundException) as e:
            print(str(e))

        input_message = input("Press q to return Main Menu: ")
        self.navigate_to_menu(input_message)
