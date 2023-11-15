package core.general_providers;
import core.enums.UserType;
import core.models.abstracts.User;
import core.models.concretes.Advisor;
import core.models.concretes.Student;


public class SessionController {
    private User currentUser;
    private static SessionController instance;

    public static SessionController getInstance(){
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }
    private SessionController(){

    }
    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    public UserType getUserType(){
        if(currentUser instanceof Student){
            return UserType.Student;
        }  
        else if(currentUser instanceof Advisor){
            return UserType.Advisor;
        }
        else{
            return null;
        }
    }
}
