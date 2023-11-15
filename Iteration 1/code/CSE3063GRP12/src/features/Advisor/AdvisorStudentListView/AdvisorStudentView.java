package features.Advisor.AdvisorStudentListView;

public class AdvisorStudentView {
    public void showStudentList(ArrayList<Student> studentArrayList){
        for(int i = 0;i<studentArrayList.size();i++){
            System.out.printf(studentArrayList.get(i));
        }
    }
}
