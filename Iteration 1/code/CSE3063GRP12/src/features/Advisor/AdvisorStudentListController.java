package features.Advisor;

import core.general_providers.TerminalManager;
import core.general_providers.SessionController;
import core.models.concretes.Advisor;
import features.main_menu.MenuController;


public class AdvisorStudentListController {
    private AdvisorStudentListView advisorStudentListView;


    public AdvisorStudentListController(){
        System.out.println("-------");
        //advisorStudentListView=new AdvisorStudentListView();
        //System.out.println("-------");
        //handleStudentList();
    }


    private void navigateToMenu(){
                System.out.println("-------");

        new MenuController();
    }

    private String getUserInput(){
                System.out.println("-------");

        String input = TerminalManager.getInstance().read();
        TerminalManager.getInstance().dispose();
        return input;
    }

    private void handleStudentList(){
                System.out.println("-------");

        while(true){
            System.out.println("Type \"q\" to see the menu\n Type \"Show\" to see the student list");
            String input = getUserInput();
            if(input.equals("q")){
                navigateToMenu();
                break;
            } else if (input.equals("Show")) {
                Advisor a = (Advisor) SessionController.getInstance().getCurrentUser();
                advisorStudentListView.showStudentList(a.getListOfStudents());
            }
        }
    }
}
