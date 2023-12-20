package core.models.concretes;

import java.util.ArrayList;

public class NotificationResponse {
    private ArrayList<Notification> listOfNotification;
    private boolean containsNew;

    public NotificationResponse(ArrayList<Notification> listOfNotification, boolean containsNew) {
        this.listOfNotification = listOfNotification;
        this.containsNew = containsNew;
    }

    public void setListOfNotification(ArrayList<Notification> listOfNotification) {
        this.listOfNotification = listOfNotification;
    }

    public ArrayList<Notification> getListOfNotification() {
        return listOfNotification;
    }

    public boolean isContainsNew() {
        return containsNew;
    }

    public void setContainsNew(boolean containsNew) {
        this.containsNew = containsNew;
    }

}