package Bank.Service.Database;

import Bank.Entity.User;

import java.sql.*;

public class DatabaseUserService {

    private Connection connection;

    public DatabaseUserService(Connection connection) {
        this.connection = connection;
    }

    public void destroyConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public boolean createUser(User user) throws SQLException {
        String query = "INSERT INTO USER VALUES (null, ?, ?, ?, ?, ?)";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setString(1, user.getLogin().toLowerCase());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getAddress());
        statement.setString(4, user.getPhone());
        statement.setLong(5, user.getCreatedAt());

        int rows = statement.executeUpdate();
        return rows != 0;
    }

    public User convertToObject(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("ID"),
                resultSet.getString("LOGIN"),
                resultSet.getString("PASSWORD"),
                resultSet.getString("ADDRESS"),
                resultSet.getString("PHONE"),
                resultSet.getLong("CREATED_AT"));
    }

    public User getUser(String login, String phone) throws SQLException {
        String query = "SELECT * FROM USER WHERE LOGIN=? OR PHONE=? LIMIT 1";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setString(1, login.toLowerCase());
        statement.setString(2, phone);
        ResultSet resultSet = statement.executeQuery();

        User user = null;
        while (resultSet.next()) {
            if (resultSet.isFirst()) {
                user = convertToObject(resultSet);
                break;
            }
        }
        resultSet.close();
        return user;
    }
}
