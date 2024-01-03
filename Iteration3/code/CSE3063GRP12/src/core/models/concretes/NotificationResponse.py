class NotificationResponse:
    def __init__(self, listOfNotification, containsNew):
        self.listOfNotification = listOfNotification  # list of Notification instances
        self.containsNew = containsNew

