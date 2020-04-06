package Bank.Service.Database;

import Bank.Entity.OperationLog;
import Bank.Service.ErrorStatus.ErrorStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperationLogService {

    private Connection connection;

    public DatabaseOperationLogService(Connection connection) {
        this.connection = connection;
    }

    public void destroyConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public boolean createOperationLog(OperationLog operationLog) throws SQLException {
        String query = "INSERT INTO OPERATIONLOG VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setInt(1, operationLog.getUserId());
        if (operationLog.getFromAccountId().isEmpty())
            statement.setNull(2, Types.VARCHAR);
        else
            statement.setString(2, operationLog.getFromAccountId());
        statement.setString(3, operationLog.getToAccountId());
        statement.setString(4, operationLog.getAccCode());
        statement.setBigDecimal(5, operationLog.getAmount());
        statement.setBigDecimal(6, operationLog.getBeforeTransfer());
        statement.setBigDecimal(7, operationLog.getAfterTransfer());
        statement.setLong(8, operationLog.getCreatedAt());

        int rows = statement.executeUpdate();
        return rows != 0;
    }

    public OperationLog convertToObject(ResultSet resultSet) throws SQLException {
        return new OperationLog(
                resultSet.getInt("ID"),
                resultSet.getInt("USER_ID"),
                resultSet.getString("FROM_ACCOUNT_ID"),
                resultSet.getString("TO_ACCOUNT_ID"),
                resultSet.getString("ACC_CODE"),
                resultSet.getBigDecimal("AMOUNT"),
                resultSet.getBigDecimal("BEFORE_TRANSFER"),
                resultSet.getBigDecimal("AFTER_TRANSFER"),
                resultSet.getLong("CREATED_AT")
        );
    }

    public List<OperationLog> getOperationLogsByAccount(String accountId) throws SQLException {
        String query = "SELECT O.* FROM OPERATIONLOG O " +
                "INNER JOIN ACCOUNT A ON O.FROM_ACCOUNT_ID = A.ID OR O.TO_ACCOUNT_ID = A.ID " +
                "WHERE (FROM_ACCOUNT_ID=? or TO_ACCOUNT_ID=?) AND O.USER_ID = A.USER_ID " +
                "ORDER BY O.CREATED_AT " +
                "GROUP BY O.ID";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setString(1, accountId);
        statement.setString(2, accountId);
        ResultSet resultSet = statement.executeQuery();

        List<OperationLog> operationLogsList = new ArrayList<OperationLog>();
        while (resultSet.next()) {
            operationLogsList.add(convertToObject(resultSet));
        }
        resultSet.close();
        return operationLogsList;
    }

    public List<OperationLog> getAllOperationLogs(int userId) throws SQLException {
        String query = "SELECT * FROM OPERATIONLOG " +
                "WHERE USER_ID = ? " +
                "ORDER BY CREATED_AT";
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        List<OperationLog> operationLogsList = new ArrayList<OperationLog>();
        while (resultSet.next()) {
            operationLogsList.add(convertToObject(resultSet));
        }
        resultSet.close();
        return operationLogsList;
    }
}
