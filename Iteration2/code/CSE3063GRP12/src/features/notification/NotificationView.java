package features.notification;

import java.util.ArrayList;

import core.models.concretes.Notification;


public class NotificationView {

    public void showNotificationList(ArrayList<Notification> notificationList){
        for (int i = notificationList.size()-1; i >= 0; i--) {
            Notification notification = notificationList.get(i);
            if(notification.isRead() == false){
                System.out.println("\033[1m" + notification.getMessage() + "\033[0m "); // Bold text using escape codes
            }
            else{
                System.out.println(notification.getMessage());
            }
        }
    }    
    public void showQuitMessage(){
        System.out.print("Press q to return Main Menu: ");
    }
}