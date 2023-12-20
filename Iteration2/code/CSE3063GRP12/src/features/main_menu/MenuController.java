package features.main_menu;

import java.util.ArrayList;

import core.enums.Menu;
import core.database.abstracts.DatabaseManager;
import core.enums.AdvisorMenu;
import core.enums.StudentMenu;

import core.enums.UserType;
import core.exceptions.UnexpectedInputException;
import core.general_providers.AppConstant;
import core.general_providers.InstanceManager;
import core.general_providers.SessionController;
import core.general_providers.TerminalManager;
import core.models.abstracts.User;
import core.models.concretes.NotificationResponse;
import core.repositories.NotificationRepositories;

public class MenuController {
    private DatabaseManager databaseManager;
    private NotificationRepositories notificationRepositories;
    private MenuView menuView;

    // Constructor
    public MenuController() {
        this.menuView = new MenuView();
        this.notificationRepositories = new NotificationRepositories();
        handleMenu();
    }

    // handle menu
    public void handleMenu() {
        // Get menu items
        ArrayList<String> menuItems = getMenuItems();
        // Show menu list
        menuView.showMenuList(menuItems);
        // Get menu selection
        String menuSelection = null;

        try{
            menuSelection = getMenuSelection();
            if(Integer.parseInt(menuSelection) < 1 || Integer.parseInt(menuSelection) > menuItems.size())
                throw new UnexpectedInputException();
        }catch(UnexpectedInputException e){
            menuView.showErrorMessage(e);
            handleMenu();
        }
        
        // Convert menu from string
        Menu menu = convertEnum(menuSelection);
        // Navigate to module
        navigateToModule(menu);
    }

    // Method to get menu items
    public ArrayList<String> getMenuItems() {
        ArrayList<String> menuItems = new ArrayList<String>();
        UserType userType = SessionController.getInstance().getUserType();
        //UserType userType = UserType.Student; //change here with upper line
        
        boolean containNew= getNotification(userType);
        switch (userType) {
            case Student:
                for (StudentMenu item : StudentMenu.values()) {
                    if( item.isNotification()&& containNew){
                        menuItems.add("\033[1m" +"*"+ item.getItemMessage() +"*"+ "\033[0m ");
                       }else{
                        menuItems.add(item.getItemMessage());
                       }
                }
                break;
            case Advisor:
                for (AdvisorMenu item : AdvisorMenu.values()) {
                    System.out.println(containNew);
                    if( item.isNotification()&& containNew){
                        menuItems.add("\033[1m" +"*"+ item.getItemMessage() +"*"+ "\033[0m ");
                       }else{
                        menuItems.add(item.getItemMessage());
                       }}
                break;
        }
        return menuItems;
    }

    public boolean getNotification(UserType userType){
        User user = SessionController.getInstance().getCurrentUser();
        NotificationResponse notificationResponse = null;
        try{
            if (userType == UserType.Student) {
                notificationResponse = notificationRepositories.getNotification(user.getUserName());
            } else if (userType == UserType.Advisor) {
                notificationResponse = notificationRepositories.getNotification(user.getUserName());
            }
            if (notificationResponse == null) {
                // throw new UserNotFoundException();
                return false;
            }else{
                return notificationResponse.isContainsNew();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // throw new UserNotFoundException();
        }

        return true;
    }
    // navigate to module
    public void navigateToModule(Menu menu) {
        menu.navigate();
    }
    
    // get menu selection from user
    public String getMenuSelection() throws UnexpectedInputException{ //this will return UnexpectedInputException
        menuView.showPromptMessage();
        String selection = TerminalManager.getInstance().read();
        // Check if selection is a number
        if(selection.matches("^\\d+$") == false){
            throw new UnexpectedInputException();
        } 
        return selection; 
    }

    // convert menu from string
    public Menu convertEnum(String menuString) {
        // convert string to Menu enum
        Menu menu;
        if (SessionController.getInstance().getUserType() == UserType.Advisor)
            menu = AdvisorMenu.values()[Integer.parseInt(menuString) - 1];
        else
            menu = StudentMenu.values()[Integer.parseInt(menuString) - 1];
        return menu;
    }
}
