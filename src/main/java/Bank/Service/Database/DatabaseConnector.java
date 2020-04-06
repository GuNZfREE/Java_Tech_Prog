package Bank.Service.Database;

import Bank.Service.ErrorStatus.ErrorStatus;
import Bank.Service.Utils.JsonLoader;
import com.google.gson.JsonObject;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    public static Connection getConnection(String path) throws
            SQLException, FileNotFoundException, ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        JsonObject jsonObject = JsonLoader.getJsonObject(path);
        return DriverManager.getConnection(jsonObject.get("db_url").getAsString(),
                jsonObject.get("db_login").getAsString(),
                jsonObject.get("db_password").getAsString()
        );
    }

    public static void runScript(String path) throws FileNotFoundException, SQLException {
        JsonObject jsonObject = JsonLoader.getJsonObject(path);
        Connection connection = DriverManager.getConnection(jsonObject.get("db_url_create").getAsString(),
                jsonObject.get("db_login").getAsString(),
                jsonObject.get("db_password").getAsString()
        );
        ScriptRunner sr = new ScriptRunner(connection);
        Reader reader = new BufferedReader(new FileReader(jsonObject.get("create_database_script").getAsString()));
        sr.runScript(reader);
        destroyConnection(connection);
    }

    public static void destroyConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
