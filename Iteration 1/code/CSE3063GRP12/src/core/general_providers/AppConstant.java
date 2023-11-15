package core.general_providers;

public class AppConstant {
    private static AppConstant instance;
    public char backKey;
    private static String backMenuMessage;

    private AppConstant(){

    }

    public static AppConstant getInstance(){
        if(instance == null){
            instance = new AppConstant();
        }
        return instance;
    }

    public String getBackMenuMessage(){
        return backMenuMessage;
    }

    public void setBackMenuMessage(String message) {
        this.backMenuMessage = message;
    }

}
