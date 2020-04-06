package Bank.Service;

import Bank.Entity.Account;
import Bank.Entity.User;
import Bank.Service.Database.DatabaseAccountService;
import Bank.Service.ErrorStatus.ErrorStatus;
import Bank.Service.Utils.Currencies;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AccountService {

    private DatabaseAccountService databaseAccountService;
    private OperationLogService operationLogService;
    private UserService userService;

    public AccountService(Connection connection) {
        this.databaseAccountService = new DatabaseAccountService(connection);
        this.operationLogService = new OperationLogService(connection);
        this.userService = new UserService(connection);
    }

    public void destroyConnection() throws SQLException {
        this.databaseAccountService.destroyConnection();
        this.operationLogService.destroyConnection();
        this.userService.destroyConnection();
    }

    public boolean checkExistAccountByPhone(String phone) throws SQLException {
        return !this.databaseAccountService.checkExistAccountsByPhone(phone);
    }

    public int createAccount(Account account) throws SQLException {
        if (this.databaseAccountService.createAccount(account)) {
            return ErrorStatus.OK;
        }
        return ErrorStatus.ACCOUNT_CREATE_ERROR;
    }

    public List<Account> getAllAccountsByUserId(int userId) throws SQLException {
        return this.databaseAccountService.getAccounts(userId);
    }

    public Account getAccount(String id) throws SQLException {
        return this.databaseAccountService.getAccount(id);
    }

    public int addMoney(String fromAccountId, String toAccountId, BigDecimal money, String accCode) throws SQLException {
        Account account = this.getAccount(toAccountId);
        if (account == null) {
            return ErrorStatus.ACCOUNT_NOT_EXIST;
        }
        String baseCur = account.getAccCode();
        BigDecimal beforeAddAmount = account.getAmount();
        BigDecimal operationAmount = null;
        if (baseCur.equals(accCode)) {
            operationAmount = money;
            account.setAmount(account.getAmount().add(money));
        }
        else {
            BigDecimal converterMoney = Currencies.converterValue(baseCur, accCode, money);
            operationAmount = converterMoney;
            account.setAmount(account.getAmount().add(converterMoney));
        }
        BigDecimal afterAddAmount = account.getAmount();
        Account accountCheckBeforeAdd = this.getAccount(toAccountId);
        if (!accountCheckBeforeAdd.getAmount().equals(beforeAddAmount)) {
            return ErrorStatus.ACCOUNT_ADD_MONEY_ERROR;
        }
        if (!this.databaseAccountService.updateMoney(toAccountId, afterAddAmount)) {
            return ErrorStatus.ACCOUNT_ADD_MONEY_ERROR;
        }

        return this.operationLogService.addOperationLog(account.getUserId(), fromAccountId, toAccountId, accCode,
                operationAmount, beforeAddAmount, afterAddAmount);
    }

    public int subMoney(String fromAccountId, String toAccountId, BigDecimal money) throws SQLException {
        Account account = this.getAccount(fromAccountId);
        if (account == null) {
            return ErrorStatus.ACCOUNT_NOT_EXIST;
        }
        BigDecimal beforeSubAmount = account.getAmount();
        BigDecimal operationAmount = money.negate();
        account.setAmount(account.getAmount().subtract(money));
        BigDecimal afterSubAmount = account.getAmount();
        if (afterSubAmount.compareTo(BigDecimal.ZERO) < 0) {
            return ErrorStatus.ACCOUNT_NOT_ENOUGHT_AMOUNT;
        }

        Account accountCheckBeforeSub = this.getAccount(fromAccountId);
        if (!accountCheckBeforeSub.getAmount().equals(beforeSubAmount)) {
            return ErrorStatus.ACCOUNT_SUB_MONEY_ERROR;
        }
        if (!this.databaseAccountService.updateMoney(fromAccountId, afterSubAmount)) {
            return ErrorStatus.ACCOUNT_SUB_MONEY_ERROR;
        }

        return this.operationLogService.addOperationLog(account.getUserId(), fromAccountId, toAccountId, account.getAccCode(),
                operationAmount, beforeSubAmount, afterSubAmount);
    }

    public int transfer(String fromAccountId, String toUserPhone, BigDecimal money) throws SQLException {
        User toUser = this.userService.getUser("", toUserPhone);
        if (toUser == null)
            return ErrorStatus.ACCOUNT_TRANSFER_MONEY_ERROR;
        List<Account> toAccounts = this.getAllAccountsByUserId(toUser.getId());
        if (toAccounts.isEmpty())
            return ErrorStatus.ACCOUNT_NOT_EXIST;
        Account fromAccount = this.getAccount(fromAccountId);
        if (fromAccount == null)
            return ErrorStatus.ACCOUNT_NOT_EXIST;
        Account toAccount = toAccounts.stream()
                                    .filter(acc -> acc.getAccCode().equals(fromAccount.getAccCode()))
                                    .findFirst().
                                    orElse(null)
                                    ;
        if (toAccount == null) {
            toAccount = toAccounts.get(0);
        }

        int check = this.subMoney(fromAccountId, toAccount.getId(), money);
        if (check != ErrorStatus.OK) return check;

        check = this.addMoney(fromAccountId, toAccount.getId(), money, fromAccount.getAccCode());
        return check;
    }
}
