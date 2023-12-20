package features.notification;

import java.io.IOException;
import java.util.ArrayList;
import core.exceptions.UserNotFoundException;
import core.general_providers.SessionController;
import core.general_providers.TerminalManager;
import core.models.abstracts.User;
import core.models.concretes.Notification;
import core.models.concretes.NotificationResponse;
import core.repositories.NotificationRepositories;
import features.main_menu.MenuController;

public class NotificationController {
    private NotificationView notificationView;
    private NotificationRepositories notificationRepositories;
    private Notification notification;
    //read = true olacak
    //notification listesini tek tek yazacak
    //q ya basarsa main menuye dönecek
    public NotificationController(){
        this.notificationView = new NotificationView();
        this.notificationRepositories = new NotificationRepositories();
        this.notification = new Notification();
        handleNotification();
    }


    private void navigateToMenu() {
        new MenuController();
    }


    private String getUserInput() {
        String input = TerminalManager.getInstance().read();
        return input;
    }

    public void handleNotification(){
        User user = SessionController.getInstance().getCurrentUser();
        NotificationResponse notificationResponse;
        try {
            notificationResponse = notificationRepositories.getNotification(user.getUserName());
            ArrayList<Notification> notifications = notificationResponse.getListOfNotification();
            notificationView.showNotificationList(notifications);
            notificationRepositories.updateNotificationRead(user.getUserName());
            notificationRepositories.updateNotificationResponseRead(user.getUserName(),false);
        } catch (IOException | UserNotFoundException e) {
            System.out.println(e.toString());
        }

        notificationView.showQuitMessage();
        String input = getUserInput();
        if (input.equals("q")) {
            navigateToMenu();
        } 
    }

}