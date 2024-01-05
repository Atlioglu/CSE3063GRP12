class NotificationResponse:
    def __init__(self, listOfNotification, containsNew):
        self.listOfNotification = listOfNotification  # list of Notification instances
        self.containsNew = containsNew
    
    def to_dict(self):
        return {
            "listOfNotification": [notification.to_dict() if hasattr(notification, 'to_dict') else notification for notification in self.listOfNotification],
            "containsNew": self.containsNew
        }

