package core.models.abstracts;

import java.util.Map;

public abstract class User {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String password;

    public abstract Map<String, Object> toJson();

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
}
