package core.repositories;

import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import core.database.abstracts.DatabaseManager;
import core.general_providers.InstanceManager;
import core.models.concretes.Advisor;
import core.models.concretes.Student;

public class UserRepository {
    private DatabaseManager databaseManager;
    private String path;

    public UserRepository(){
        databaseManager= InstanceManager.getInstance().getDataBaseInstance();

    }
    public void loginCheck(String userName, String password) throws IOException {
        try{
            Map<String, Object> map = databaseManager.read(path);
            if(userName == map.get("username")&& password == map.get("password")) {
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(String username) throws IOException{
        Map<String, Object> map = databaseManager.read(path);
        username=(String) map.get("username");
        char firstChar = username.charAt(0);

        if(firstChar =='o'){

        } else if(firstChar =='a'){
            
        }
    }


    public ArrayList<Student> getStudentsByAdvisor(Advisor advisor){

        
        return null;
    }
    
}
