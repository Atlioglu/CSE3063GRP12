import os
from datetime import datetime

from core.general_providers.AppConstant import AppConstant
from core.general_providers.InstanceManager import InstanceManager
from core.enums.UserType import UserType
from core.models.concretes.NotificationResponse import NotificationResponse
from core.models.concretes.Notification import Notification

class NotificationRepository:
    def __init__(self):
        self.database_manager = InstanceManager().get_database_instance()
        self.path = os.getcwd() + AppConstant.getInstance().get_base_path() + "notification/"
        self.student_path = os.path.join(self.path, "student")
        self.advisor_path = os.path.join(self.path, "advisor")

    def get_notification(self, user_name):
        user_type = self.get_user_type(user_name)
        notification_response = None
        try:
            if user_type == UserType.Student:
                file_path = f"{self.student_path}/{user_name}.json"
                notification_response = self.database_manager.read(file_path,NotificationResponse)
            elif user_type == UserType.Advisor:
                file_path = f"{self.advisor_path}/{user_name}.json"
                notification_response = self.database_manager.read(file_path,NotificationResponse)
            if notification_response is None:
                return None
        except Exception as e:
           print(f"An error occurred while getting notification: {e}")

        return notification_response
    
    def write_notification(self, user_name, notification_response):
        user_type = self.get_user_type(user_name)
        try:
            file_path = os.path.join(self.advisor_path if user_type == UserType.Advisor else self.student_path, user_name + ".json")
            self.database_manager.write(file_path, notification_response)
        except Exception as e:
            print(f"An error occurred while writing notification: {e}")
            pass
    
    def update_notification(self, user_name, message):
        user_type = self.get_user_type(user_name)
        notification_response = None

        try:
            if user_type == UserType.Student:
                notification_response = self.database_manager.read(f"{self.student_path}/{user_name}.json", NotificationResponse)
            elif user_type == UserType.Advisor:
                notification_response = self.database_manager.read(f"{self.advisor_path}/{user_name}.json", NotificationResponse)

            date = datetime.now()
            notification = Notification(message, date, False)

            if notification_response:
                notification_list = notification_response.listOfNotification
                if notification_list is None:
                    notification_list = []
                notification_list.append(notification)
            else:
                notification_response = NotificationResponse([], True)
                notification_response.listOfNotification.append(notification)

            self.write_notification(user_name, notification_response)
            self.update_notification_response_read(user_name, True)

        except Exception as e:
            print(e)
    
    def update_notification_read(self, user_name):
        user_type = self.get_user_type(user_name)
        notification_response = None

        try:
            if user_type == UserType.Student:
                notification_response = self.database_manager.read(f"{self.student_path}/{user_name}.json",NotificationResponse)
            elif user_type == UserType.Advisor:
                notification_response = self.database_manager.read(f"{self.advisor_path}/{user_name}.json",NotificationResponse)

            notifications = notification_response.listOfNotification
            for notification in reversed(notifications):
                notification.set_read(True)

            self.write_notification(user_name, notification_response)

        except Exception as e:
            print(e)

    def update_notification_response_read(self, user_name, contain_new):
        user_type = self.get_user_type(user_name)
        notification_response = None

        try:
            if user_type == UserType.Student:
                notification_response = self.database_manager.read(f"{self.student_path}/{user_name}.json",NotificationResponse)
            elif user_type == UserType.Advisor:
                notification_response = self.database_manager.read(f"{self.advisor_path}/{user_name}.json", NotificationResponse)

            notification_response.containsNew = contain_new
            self.write_notification(user_name, notification_response)

        except Exception as e:
            print(f"Error: in update_notification_response_read {e}")

    
    def get_user_type(self, user_name):
        first_letter = user_name[0]
        if first_letter == "o":
            return UserType.Student
        elif first_letter == "a":
            return UserType.Advisor
        else:
            return None

