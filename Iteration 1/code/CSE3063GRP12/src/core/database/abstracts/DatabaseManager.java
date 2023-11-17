package core.database.abstracts;

import java.io.IOException;

public abstract class DatabaseManager {
    public abstract <T> T read(String path, Class<T> classOfT) throws IOException;

    public abstract <T> void write(String path, T object) throws IOException;

}
