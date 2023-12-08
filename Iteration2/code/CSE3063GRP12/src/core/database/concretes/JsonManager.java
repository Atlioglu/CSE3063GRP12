package core.database.concretes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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
            return gson.fromJson(reader, classOfT);
        } catch (Exception e) {
            //e.printStackTrace();
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
