package Bank.Service.Database;

import Bank.Entity.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccountService {

    private Connection connection;

    public DatabaseAccountService(Connection connection) {
        this.connection = connection;
    }

    public void destroyConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public boolean createAccount(Account account) throws SQLException {
        String query = "INSERT INTO ACCOUNT VALUES (uuid(), ?, ?, ?, ?, null)";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setInt(1, account.getUserId());
        statement.setBigDecimal(2, account.getAmount());
        statement.setString(3, account.getAccCode());
        statement.setLong(4, account.getCreatedAt());

        int rows = statement.executeUpdate();
        return rows != 0;
    }

    public Account convertToObject(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getString("ID"),
                resultSet.getInt("USER_ID"),
                resultSet.getBigDecimal("AMOUNT"),
                resultSet.getString("ACC_CODE"),
                resultSet.getLong("CREATED_AT"),
                resultSet.getLong("DELETED_AT"));
    }

    public Account getAccount(String id) throws SQLException {
        String query = "SELECT * FROM ACCOUNT WHERE ID=?";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();

        Account account = null;
        while (resultSet.next()) {
            if (resultSet.isFirst()) {
                account = convertToObject(resultSet);
                break;
            }
        }
        resultSet.close();
        return account;
    }

    public List<Account> getAccounts(int userId) throws SQLException {
        String query = "SELECT * FROM ACCOUNT WHERE USER_ID=?";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        List<Account> accountsList = new ArrayList<Account>();
        while (resultSet.next()) {
            accountsList.add(convertToObject(resultSet));
        }
        resultSet.close();
        return accountsList;
    }

    public Account getAccountByRates(int userId, String accCode) throws SQLException {
        String query = "SELECT * FROM ACCOUNT WHERE USER_ID=? AND ACC_CODE=?";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, accCode);
        ResultSet resultSet = statement.executeQuery();

        Account account = null;
        while (resultSet.next()) {
            if (resultSet.isFirst()) {
                account = convertToObject(resultSet);
                break;
            }
        }
        resultSet.close();
        return account;
    }

    public boolean updateMoney(String id, BigDecimal money) throws SQLException {
        String query = "UPDATE ACCOUNT SET AMOUNT=? WHERE ID=?";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setBigDecimal(1, money);
        statement.setString(2, id);

        int rows = statement.executeUpdate();
        return rows != 0;
    }

    public boolean checkExistAccountsByPhone(String phone) throws SQLException {
        String query = "SELECT A.* FROM ACCOUNT A " +
                "INNER JOIN USER U ON A.USER_ID = U.ID " +
                "WHERE U.PHONE = ?";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setString(1, phone);
        ResultSet resultSet = statement.executeQuery();

        List<Account> accountsList = new ArrayList<Account>();
        while (resultSet.next()) {
            accountsList.add(convertToObject(resultSet));
        }
        resultSet.close();
        return accountsList.isEmpty();
    }
}
