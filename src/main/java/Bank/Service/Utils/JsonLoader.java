package Bank.Service.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonLoader {
    public static JsonObject getJsonObject(String path) throws FileNotFoundException {
        Gson gson = new GsonBuilder().create();
        FileReader json = new FileReader(path);
        return gson.fromJson(json, JsonObject.class);
    }
}
