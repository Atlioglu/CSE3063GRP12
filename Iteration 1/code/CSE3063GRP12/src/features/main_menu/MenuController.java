package features.main_menu;

import java.util.ArrayList;

import core.enums.Menu;
import core.enums.AdvisorMenu;
import core.enums.StudentMenu;

import core.enums.UserType;
import core.general_providers.SessionController;
import core.general_providers.TerminalManager;

public class MenuController {

    private MenuView menuView;

    // Constructor
    public MenuController() {
        this.menuView = new MenuView();
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
        }catch(IllegalArgumentException e){
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
        // UNCOMMENT: UserType userType = SessionController.getInstance().getUserType();
        UserType userType = UserType.Student; //change here with upper line
        switch (userType) {
            case Student:
                for (StudentMenu item : StudentMenu.values()) {
                    menuItems.add(item.getItemMessage());
                }
                break;
            case Advisor:
                for (AdvisorMenu item : AdvisorMenu.values()) {
                    menuItems.add(item.getItemMessage());
                }
                break;
        }
        return menuItems;
    }

    // Method to navigate to module
    public void navigateToModule(Menu menu) {
        menu.navigate();
    }
    
    // Method to get menu selection from user
    public String getMenuSelection() throws IllegalArgumentException{ //this will return UnexpectedInputException
        menuView.showPromptMessage();
        String selection = TerminalManager.getInstance().read();
        // Check if selection is a number
        if(selection.matches("^\\d+$") == false){
            // UNCOMMENT: throw new UnexpectedInputException();
            throw new IllegalArgumentException("Please enter a valid number.\n"); // TODO: change here with upper line
        } 
        return selection; 
    }

    // Method to convert menu from string
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
