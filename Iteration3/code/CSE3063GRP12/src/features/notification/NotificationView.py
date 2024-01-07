class NotificationView:

    def show_notification_list(self, notification_list):
            # Iterating through the list in reverse order as in the original Java code
            for notification in reversed(notification_list):
                # Check if the notification is unread
                if not notification.get("is_read"):
                    # Printing in bold in Python is a bit different than in Java
                    print(f"\033[1m{notification.get('message')}\033[0m")  # Bold text
                else:
                    print(notification.get('message'))