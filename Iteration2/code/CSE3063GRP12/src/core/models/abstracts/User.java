package core.models.abstracts;

import java.util.Objects;

import core.enums.UserType;
import core.models.concretes.Student;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Student student = (Student) obj;
        return Objects.equals(userName, student.userName) &&
                Objects.equals(password, student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }
}