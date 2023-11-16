package features.main_menu;

import java.util.ArrayList;

public class MenuView {

 // show menu list
 public void showMenuList(ArrayList<String> menuList) {
    // Simply print out each menu item
    // i for indexing
    for (int i=1; i<=menuList.size(); i++) {
        System.out.println(i + ". " + menuList.get(i-1));
    }
}

// show error message
public void showErrorMessage(Exception e) {
    System.out.println("Error: " + e.getMessage());
}

public void showPromptMessage() {
    System.out.print("Please enter your selection: ");
}

}