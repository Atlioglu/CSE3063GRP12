package core.login;

import core.repositories.UserRepository;
import core.models.abstracts.User;
import core.general_providers.TerminalManager;
import core.models.abstracts.User;
import core.models.concretes.Student;
import core.models.concretes.Advisor;
import core.enums.UserType;
//import core.main_menu.MenuController;

public class LoginController {
    private LoginView loginView;
    private UserRepository userRepository;
    
    // Constructor
    public LoginController() {
        this.loginView = new LoginView();
        this.userRepository = new UserRepository();
        handleLogin();
    }

    // navigate to the menu
    public void navigateToMenu() {
       // new MenuController();
    }

    // get username input from the user
    public String getUserNameInput() {
        loginView.displayUsername();
        return TerminalManager.getInstance().read(); // Assuming TerminalManager.getInstance(); has a read method to get input.
    }

    // get password input from the user
    public String getPasswordInput() {
        loginView.displayPassword();
        return TerminalManager.getInstance().read(); // Assuming the same read method is used for password.
    }

    // handle the login logic
    public void handleLogin() {
        String username = getUserNameInput();
        String password = getPasswordInput();
        try {
            userRepository.loginCheck(username, password);
            navigateToMenu();
        } catch (Exception e) {
            loginView.showError(e);
        }
    }
}