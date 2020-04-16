package ru.Bank.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Bank.Entity.Account;
import ru.Bank.Entity.OperationLog;
import ru.Bank.Entity.User;
import ru.Bank.Helper.Status.ErrorStatus;
import ru.Bank.Model.Request.OperationAddByPhoneRequest;
import ru.Bank.Model.Request.OperationAddOnAccountRequest;
import ru.Bank.Model.Request.OperationTransferRequest;
import ru.Bank.Repository.AccountRepository;
import ru.Bank.Repository.OperationLogRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OperationLogService extends AbstractService {
    private final OperationLogRepository operationLogRepository;

    @Autowired
    public OperationLogService(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    public List<OperationLog> getHistoryByUser(User user) {
        return operationLogRepository.findAllByUser(user);
    }

    public List<OperationLog> getHistoryByAccount(Account account) {
        return operationLogRepository.findAllByFromAccountOrToAccount(account, account);
    }

    public ErrorStatus checkMoney(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0)
            return ErrorStatus.OK;
        return ErrorStatus.OPERATION_INCORRECT_AMOUNT;
    }

    public ErrorStatus addOperationLog(User user, Account fromAccount, Account toAccount,
                                       String accCode, BigDecimal amount,
                                       BigDecimal beforeTransfer, BigDecimal afterTransfer) {
        OperationLog operationLog = new OperationLog(user, fromAccount, toAccount, accCode, amount,
                beforeTransfer, afterTransfer);
        operationLogRepository.save(operationLog);
        return ErrorStatus.OK;
    }
}
