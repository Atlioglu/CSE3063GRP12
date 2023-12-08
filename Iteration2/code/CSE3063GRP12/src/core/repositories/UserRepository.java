package core.repositories;

import java.util.ArrayList;

import com.google.gson.Gson;

import java.io.IOException;
import core.database.abstracts.DatabaseManager;
import core.enums.UserType;
import core.exceptions.UserNotFoundException;
import core.exceptions.WrongPasswordException;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.general_providers.SessionController;
import core.models.abstracts.User;
import core.models.concretes.Advisor;
import core.models.concretes.Student;

public class UserRepository {
    private DatabaseManager databaseManager;
    private String path;
    private String advisorPath;
    private String studentPath;

    public UserRepository() {
        path = System.getProperty("user.dir") + AppConstant.getInstance().getBasePath() + "/user/";
        advisorPath = path + "advisor/";
        studentPath = path + "student/";
        databaseManager = InstanceManager.getInstance().getDataBaseInstance();

    }

    public void loginCheck(String userName, String password)
            throws UserNotFoundException, WrongPasswordException {
        try {
            User user = null;
            if (getUserType(userName) == UserType.Student) {
                user = databaseManager.read(studentPath + "/" + userName + ".json", Student.class);
                if (user.getPassword().equals(password)) {
                    setCurrentUser(user);
                } else {
                    throw new WrongPasswordException();
                }

            } else if (getUserType(userName) == UserType.Advisor) {
                user = databaseManager.read(advisorPath + "/" + userName + ".json", Advisor.class);
                if (user.getPassword().equals(password)) {
                    setCurrentUser(user);

                } else {
                    throw new WrongPasswordException();
                }

            }
            // TODO: PROBABLY IT WONT WORK
            if (user == null) {
                throw new UserNotFoundException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserNotFoundException();
        }
    }

    public void setCurrentUser(User user) throws IOException {
        SessionController.getInstance().setCurrentUser(user);
    }

    public ArrayList<Student> getStudentsByAdvisor(Advisor advisor) {
        try {
            ArrayList<Student> students = new ArrayList<Student>();
            for (String ids : advisor.getListOfStudentIds()) {
                Student student = databaseManager.read(studentPath + "/" + ids + ".json", Student.class);
                students.add(student);
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUser(String userName) throws UserNotFoundException {
        User user = null;
        try {

            if (getUserType(userName) == UserType.Student) {
                user = databaseManager.read(studentPath + "/" + userName + ".json", Student.class);
            } else if (getUserType(userName) == UserType.Advisor) {
                user = databaseManager.read(advisorPath + "/" + userName + ".json", Advisor.class);
            }
            // TODO: PROBABLY IT WONT WORK
            if (user == null) {
                throw new UserNotFoundException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserNotFoundException();
        }
        return user;

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
