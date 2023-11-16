package features.Advisor;

import core.general_providers.TerminalManager;
import core.general_providers.SessionController;
import core.models.concretes.Advisor;


public class AdvisorStudentListController {
    private AdvisorStudentListView advisorStudentListView;

    public AdvisorStudentListController(){
        advisorStudentListView=new AdvisorStudentListView();

        handleStudentList();
    }


    private void navigateToMenu(){
        return new MenuController();
    }

    private String getUserInput(){
        String input = TerminalManager.getInstance().read();
        TerminalManager.getInstance().dispose();
        return input;
    }

    private void handleStudentList(){
        while(true){
            System.out.printf("Type \"Quit\" to see the menu\n Type \"Show\" to see the student list");
            String input = getUserInput();
            if(input.equals("Quit")){
                navigateToMenu();
                break;
            } else if (input.equals("Show")) {
                advisorStudentListView.showStudentList(SessionController.getInstance().getCurrentUser().getListOfStudents());
            }
        }
    }
}
