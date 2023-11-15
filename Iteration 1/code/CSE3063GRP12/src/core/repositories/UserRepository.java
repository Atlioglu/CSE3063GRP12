package core.repositories;

import java.util.ArrayList;

import core.database.abstracts.DatabaseManager;
import core.models.concretes.Advisor;
import core.models.concretes.Student;

public class UserRepository {
    private DatabaseManager databaseManager;
    private String path;

    public UserRepository(){

    }
    public void loginCheck(String userName, String password){

    }
    public ArrayList<Student> getStudentsByAdvisor(Advisor advisor){

        
        return null;
    }
    
}
