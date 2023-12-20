package core.repositories;

import java.util.ArrayList;
import java.io.IOException;
import core.database.abstracts.DatabaseManager;
import core.enums.UserType;
import core.exceptions.UserNotFoundException;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.models.concretes.Notification;
import core.models.concretes.NotificationResponse;

public class NotificationRepositories {
    private DatabaseManager databaseManager;
    private String path, studentPath, advisorPath;

    public NotificationRepositories() {
        databaseManager = InstanceManager.getInstance().getDataBaseInstance();
        path = System.getProperty("user.dir") + AppConstant.getInstance().getBasePath() + "/notification/";
        advisorPath = path + "advisor";
        studentPath = path + "student";
    }

    public NotificationResponse getNotification(String userName) throws IOException, UserNotFoundException {
        UserType userType = getUserType(userName);
        NotificationResponse notificationResponse = null;
        try {
            if (userType == UserType.Student) {
                notificationResponse = databaseManager.read(studentPath + "/" + userName + ".json",
                        NotificationResponse.class);
            } else if (userType == UserType.Advisor) {
                notificationResponse = databaseManager.read(advisorPath + "/" + userName + ".json",
                        NotificationResponse.class);
            }
            if (notificationResponse == null) {
                // throw new UserNotFoundException();
                return null;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            // throw new UserNotFoundException();
            return null;
        }
        return notificationResponse;
    }

    public void writeNotification(String userName, NotificationResponse notificationResponse) {
        UserType userType = getUserType(userName);
        try {
            if (userType == UserType.Student) {
                databaseManager.write(studentPath + "/" + userName + ".json", notificationResponse);
            } else if (userType == UserType.Advisor) {
                databaseManager.write(advisorPath + "/" + userName + ".json", notificationResponse);
            }

        } catch (Exception e) {
            // e.printStackTrace();
            // throw new UserNotFoundException();
        }
    }

    public void updateNotification(String userName, String message) {
        UserType userType = getUserType(userName);
        NotificationResponse notificationResponse = null;
        try {
            if (userType == UserType.Student) {
                notificationResponse = databaseManager.read(studentPath + "/" + userName + ".json",
                        NotificationResponse.class);
            } else if (userType == UserType.Advisor) {
                notificationResponse = databaseManager.read(advisorPath + "/" + userName + ".json",
                        NotificationResponse.class);
            }
            java.util.Date date = new java.util.Date();
            Notification notification = new Notification(message, date, false);
            if (notificationResponse != null) {
                ArrayList<Notification> notificationList = notificationResponse.getListOfNotification();
                if (notificationList == null) {
                    notificationList = new ArrayList<>();
                }
                notificationList.add(notification);
            } else {
                notificationResponse = new NotificationResponse(new ArrayList<>(), true);
                notificationResponse.getListOfNotification().add(notification);
            }
            writeNotification(userName, notificationResponse);
            updateNotificationResponseRead(userName, true);

        } catch (Exception e) {
            // e.printStackTrace();

            System.out.println(e.getMessage());
            // throw new UserNotFoundException();
        }
    }

    public void updateNotificationRead(String userName) {
        UserType userType = getUserType(userName);
        NotificationResponse notificationResponse = null;
        try {
            if (userType == UserType.Student) {
                notificationResponse = databaseManager.read(studentPath + "/" + userName + ".json",
                        NotificationResponse.class);
            } else if (userType == UserType.Advisor) {
                notificationResponse = databaseManager.read(advisorPath + "/" + userName + ".json",
                        NotificationResponse.class);
            }
            ArrayList<Notification> notifications = notificationResponse.getListOfNotification();
            for (int i = notifications.size() - 1; i >= 0; i--) {
                Notification notification = notifications.get(i);
                notification.setRead(true);
            }
            writeNotification(userName, notificationResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateNotificationResponseRead(String userName, boolean containNew) {
        UserType userType = getUserType(userName);
        NotificationResponse notificationResponse = null;
        try {
            if (userType == UserType.Student) {
                notificationResponse = databaseManager.read(studentPath + "/" + userName + ".json",
                        NotificationResponse.class);
            } else if (userType == UserType.Advisor) {
                notificationResponse = databaseManager.read(advisorPath + "/" + userName + ".json",
                        NotificationResponse.class);
            }
            notificationResponse.setContainsNew(containNew);
            writeNotification(userName, notificationResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private UserType getUserType(String userName) {
        char firstChar = userName.charAt(0);
        if (firstChar == 'o') {
            return UserType.Student;
        } else if (firstChar == 'a') {
            return UserType.Advisor;
        }
        return null;
    }

}