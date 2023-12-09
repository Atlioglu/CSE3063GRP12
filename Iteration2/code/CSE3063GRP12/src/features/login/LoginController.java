package features.login;

import core.repositories.UserRepository;
import features.main_menu.MenuController;
import core.general_providers.TerminalManager;

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
        new MenuController();
    }

    // get username input from the user
    public String getUserNameInput() {
        loginView.displayUsername();
        return TerminalManager.getInstance().read(); 
    }

    // get password input from the user
    public String getPasswordInput() {
        loginView.displayPassword();
        return TerminalManager.getInstance().read();
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
            handleLogin();
        }
    }
}