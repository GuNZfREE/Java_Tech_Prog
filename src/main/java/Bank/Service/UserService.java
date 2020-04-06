package Bank.Service;

import Bank.Entity.User;
import Bank.Service.Database.DatabaseUserService;
import Bank.Service.ErrorStatus.ErrorStatus;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserService {
    private Scanner scanner;
    private DatabaseUserService databaseUserService;

    public UserService(Connection connection) {
        this.databaseUserService = new DatabaseUserService(connection);
        this.scanner = new Scanner(System.in);
    }

    public void destroyConnection() throws SQLException {
        this.databaseUserService.destroyConnection();
    }

    public boolean checkExistUserByLogin(String login) throws SQLException {
        return this.databaseUserService.getUser(login.toLowerCase(), "") != null;
    }

    public boolean checkExistUserByPhone(String phone) throws SQLException {
        return this.databaseUserService.getUser("", phone) != null;
    }

    public User getUser(String login, String phone) throws SQLException {
        return this.databaseUserService.getUser(login, phone);
    }

    public int registration(User user) throws SQLException {
        if (!this.databaseUserService.createUser(user)) {
            return ErrorStatus.USER_CREATE_ERROR;
        }
        return ErrorStatus.OK;
    }

    public String cryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean comparePassword(String cryptPassword, String inputPassword) {
        return BCrypt.checkpw(inputPassword, cryptPassword);
    }

    public User login(String log, String password) throws SQLException {
        User user = this.databaseUserService.getUser(log.toLowerCase(), log);
        if (user == null) {
            return null;
        }
        if (comparePassword(user.getPassword(), password)) {
            return user;
        }
        return null;
    }

    public String checkEmptyData(String data, String name) {
        while(data.isEmpty()) {
            System.out.println("Введите " + name + ": ");
            data = this.scanner.next();
        }
        return data;
    }
}
