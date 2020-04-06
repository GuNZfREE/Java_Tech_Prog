package Bank.Service;

import Bank.Entity.OperationLog;
import Bank.Service.Database.DatabaseOperationLogService;
import Bank.Service.ErrorStatus.ErrorStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OperationLogService {

    private DatabaseOperationLogService databaseOperationLogService;

    public OperationLogService(Connection connection) {
        this.databaseOperationLogService = new DatabaseOperationLogService(connection);
    }

    public void destroyConnection() throws SQLException {
        this.databaseOperationLogService.destroyConnection();
    }

    public int createOperationLog(OperationLog operationLog) throws SQLException {
        if (this.databaseOperationLogService.createOperationLog(operationLog)) {
            return ErrorStatus.OK;
        }
        return ErrorStatus.OPERATIONLOG_CREATE_ERROR;
    }

    public List<OperationLog> getAllOperationLogByUserId(int userId) throws SQLException {
        return this.databaseOperationLogService.getAllOperationLogs(userId);
    }

    public int addOperationLog(int userId, String  fromAccountId, String  toAccountId, String accCode, BigDecimal amount,
                               BigDecimal beforeAdd, BigDecimal afterAdd) throws SQLException {
        OperationLog operationLog = new OperationLog(userId, fromAccountId, toAccountId, accCode, amount, beforeAdd, afterAdd);
        return createOperationLog(operationLog);
    }
}
