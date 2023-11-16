package features.Advisor;

import core.models.concretes.Student;
import java.util.ArrayList;

public class AdvisorStudentListView {
    public void showStudentList(ArrayList<Student> studentArrayList){
        for(int i = 0;i<studentArrayList.size();i++){
            System.out.printf(studentArrayList.get(i).getFirstName());
            System.out.printf(studentArrayList.get(i).getLastName());
        }
    }
}
