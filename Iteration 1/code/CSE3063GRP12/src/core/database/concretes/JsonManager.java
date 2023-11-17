package core.database.concretes;

import java.util.Map;
import java.io.IOException;

import core.database.abstracts.DatabaseManager;


public class JsonManager extends DatabaseManager {
    public Map<String,Object> read(String path) throws IOException{
        /*Gson gson = new Gson();
        path = "Studentt.json";
        try {
            FileReader reader = new FileReader(path);
            return gson.fromJson(reader, new TypeToken<Map<String, Object>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();*/
            return null;
        }



         
    public void write(String path, Map<String,Object> map) throws IOException{
       /*  Gson gson = new Gson();
            String json = gson.toJson(map);
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        */
        }
    }
