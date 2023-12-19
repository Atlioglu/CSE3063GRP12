package features.advisor.student_list;

import core.exceptions.UnexpectedInputException;
import core.models.concretes.Student;
import java.util.ArrayList;

public class AdvisorStudentListView{
    public void showStudentList(ArrayList<Student> studentArrayList){
        for(int i = 1;i<=studentArrayList.size();i++){
            System.out.println(i + ". " + studentArrayList.get(i-1).getUserName() + " " + studentArrayList.get(i-1).getLastName());
        }
    }
    public void showQuitMessage(){
        System.out.print("Press q to return Main Menu: ");
    }
    public void showErrorMessage() throws UnexpectedInputException{
        throw new UnexpectedInputException();
    }

}
