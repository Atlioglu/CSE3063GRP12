package core.general_providers;
import core.database.abstracts.DatabaseManager;
import core.database.concretes.JsonManager;

public class InstanceManager {
    private static InstanceManager instance;

    private InstanceManager() {
        
    }

    public static InstanceManager getInstance() {
        if (instance == null) {
            instance = new InstanceManager();
        }
        return instance;
    }

    public DatabaseManager getDataBaseInstance() {
        
        return new JsonManager();
    }
}
