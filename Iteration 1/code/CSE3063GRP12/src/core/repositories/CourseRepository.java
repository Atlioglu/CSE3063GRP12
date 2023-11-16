package core.repositories;

import java.util.ArrayList;
import core.database.abstracts.DatabaseManager;
import core.general_providers.InstanceManager;
import core.models.concretes.Course;

public class CourseRepository{
    private DatabaseManager databaseManager;
    private String path;

    public CourseRepository(){
                databaseManager= InstanceManager.getInstance().getDataBaseInstance();

    }
    public ArrayList<Course> getCoursesBySemester(int id){
        return null;
    }
}
