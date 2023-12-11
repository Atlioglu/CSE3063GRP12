package core.database.concretes;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import core.database.abstracts.DatabaseManager;

public class JsonManager extends DatabaseManager {
    private Gson gson;

    public JsonManager() {

        gson = new Gson();
    }

    public <T> T read(String path, Class<T> classOfT) {
        try (FileReader reader = new FileReader(path)) {
            var result = gson.fromJson(reader, classOfT);
            return result;
        } catch (Exception e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());
            return null;
        }
    }

    public <T> void write(String path, T object) {
        String json = gson.toJson(object);
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}
