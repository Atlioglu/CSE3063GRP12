package core.login;

public class LoginView {
    
    // Prompt the user to enter their username
    public void displayUsername() {
        System.out.println("Please enter your username:");
    }

    // Prompt the user to enter their password
    public void displayPassword() {
        System.out.println("Please enter your password:");
    }

    // Display an error message
    public void showError(Exception exception) {
        System.out.println("Error: " + exception.getMessage());
    }

}
