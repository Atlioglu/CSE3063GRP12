package core.models.abstracts;

import core.enums.UserType;

public abstract class User {

    protected String id;

    protected String firstName;

    protected String lastName;

    protected String userName;

    protected String password;

    // Setter methods
    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter methods
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        char firstChar = getUserName().charAt(0);
        if (firstChar == 'o') {
            return UserType.Student;
        } else if (firstChar == 'a') {
            return UserType.Advisor;
        }
        return null;
    }
}