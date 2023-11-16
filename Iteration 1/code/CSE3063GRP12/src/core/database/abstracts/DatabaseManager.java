package core.database.abstracts;

import java.io.IOException;
import java.util.Map;

public abstract class DatabaseManager {
    public abstract Map<String,Object> read(String path)throws IOException;
    public abstract void write(String path, Map<String,Object> map) throws IOException;
    
}
