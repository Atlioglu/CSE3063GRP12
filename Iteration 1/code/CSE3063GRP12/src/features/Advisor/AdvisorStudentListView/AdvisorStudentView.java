package features.Advisor.AdvisorStudentListView;

public class AdvisorStudentView {
    public void showStudentList(ArrayList<Student> studentArrayList){
        for(int i = 0;i<studentArrayList.size();i++){
            System.out.printf(studentArrayList.get(i).getFirstName());
            System.out.printf("\s");
            System.out.printf(studentArrayList.get(i).getLastName());
            System.out.printf("\n");
        }
    }
}
