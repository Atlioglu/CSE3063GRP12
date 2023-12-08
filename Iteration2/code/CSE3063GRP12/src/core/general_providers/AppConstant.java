package core.general_providers;

public class AppConstant {
    private static AppConstant instance;
    public char backKey;
    private static String backMenuMessage;
    private String basePath;

    private AppConstant() {
        backMenuMessage = ("Do you want to go back");
        basePath = "/Iteration 1/code/CSE3063GRP12/database";
    }

    public static AppConstant getInstance() {
        if (instance == null) {
            instance = new AppConstant();
        }
        return instance;
    }

    public String getBackMenuMessage() {
        return backMenuMessage;
    }

    public void setBackMenuMessage(String message) {
        backMenuMessage = message;
    }

    public String getBasePath() {
        return basePath;
    }

}