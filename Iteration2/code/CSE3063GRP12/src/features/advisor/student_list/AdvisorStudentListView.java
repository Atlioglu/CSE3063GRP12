package features.advisor.student_list;

import core.models.concretes.Student;
import java.util.ArrayList;

public class AdvisorStudentListView {
    public void showStudentList(ArrayList<Student> studentArrayList){
        System.out.println("======================================================================================Student List======================================================================================");
        System.out.printf("%-27s%-20s%-20s\n", "[Student ID]", "[First Name]", "[Last Name]");
        System.out.println("========================================================================================================================================================================================");
        for(int i = 1;i<=studentArrayList.size();i++){
            System.out.printf("[%-2d] %-20s %-20s %-20s\n", i, studentArrayList.get(i - 1).getUserName(), studentArrayList.get(i - 1).getFirstName(), studentArrayList.get(i - 1).getLastName());
        }
        System.out.println("========================================================================================================================================================================================");
    }
    public void showQuitMessage(){
        System.out.print("Press q to return Main Menu: ");
    }

}
