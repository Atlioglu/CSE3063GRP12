package features.advisor;

import core.models.concretes.Student;
import java.util.ArrayList;

public class AdvisorStudentListView {
    public void showStudentList(ArrayList<Student> studentArrayList){
        for(int i = 1;i<=studentArrayList.size();i++){
            System.out.println(i + ". " + studentArrayList.get(i-1).getUserName() + " " + studentArrayList.get(i-1).getLastName());
        }
    }
    public void showQuitMessage(){
        System.out.print("Press q to return Main Menu: ");
    }

}
