package ru.Bank.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Bank.Entity.Account;
import ru.Bank.Entity.User;
import ru.Bank.Helper.Status.ErrorStatus;
import ru.Bank.Helper.Utils.Currencies;
import ru.Bank.Model.Request.OperationTransferRequest;
import ru.Bank.Repository.AccountRepository;
import ru.Bank.Repository.UserRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService extends AbstractService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final OperationLogService operationLogService;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository, OperationLogService operationLogService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.operationLogService = operationLogService;
    }

    public boolean checkCurrience(String accCode) {
        return Currencies.currencies.contains(accCode.toUpperCase());
    }

    public ErrorStatus checkOwn(Account account, User user) {
        if (account.getUser().getId().equals(user.getId()))
            return ErrorStatus.OK;
        return ErrorStatus.ACCOUNT_NOT_OWN;
    }

    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findAllByUserAndDeletedAtIsNull(user);
    }

    public Account getAccountByUuid(UUID uuid) {
        return accountRepository.findAccountByIdAndDeletedAtIsNull(uuid);
    }

    public Account getAccountForOperation(User user, String accCode, UUID fromAccountId) {
        List<Account> toAccounts = this.getAccountsByUser(user);
        if (toAccounts.isEmpty())
            return null;
        Account toAccount = toAccounts.stream()
                .filter(acc -> acc.getAccCode().equals(accCode) && !acc.getId().equals(fromAccountId))
                .findFirst().
                        orElse(null)
                ;
        if (toAccount == null) {
            toAccount = toAccounts.get(0);
        }
        return toAccount;
    }

    public ErrorStatus addMoney(Account fromAccount, Account toAccount, String accCode, BigDecimal amount) {
        String baseCur = toAccount.getAccCode();
        BigDecimal beforeAddAmount = toAccount.getAmount();
        if (baseCur.equals(accCode)) {
            toAccount.setAmount(toAccount.getAmount().add(amount));
        }
        else {
            BigDecimal converterMoney = Currencies.converterValue(baseCur, accCode, amount);
            toAccount.setAmount(toAccount.getAmount().add(converterMoney));
        }
        BigDecimal afterAddAmount = toAccount.getAmount();
        toAccount = this.accountRepository.save(toAccount);
        if (toAccount == null)
            return ErrorStatus.ACCOUNT_ADD_MONEY_ERROR;
        return this.operationLogService.addOperationLog(toAccount.getUser(), fromAccount, toAccount, accCode, amount,
                beforeAddAmount, afterAddAmount);
    }

    public ErrorStatus subMoney(Account fromAccount, Account toAccount, OperationTransferRequest operationTransferRequest) {
        BigDecimal beforeSubAmount = fromAccount.getAmount();
        fromAccount.setAmount(fromAccount.getAmount().subtract(operationTransferRequest.getAmount()));
        BigDecimal afterSubAmount = fromAccount.getAmount();
        if (afterSubAmount.compareTo(BigDecimal.ZERO) < 0) {
            return ErrorStatus.ACCOUNT_NOT_ENOUGHT_AMOUNT;
        }
        fromAccount = this.accountRepository.save(fromAccount);
        if (fromAccount == null)
            return ErrorStatus.ACCOUNT_ADD_MONEY_ERROR;

        return this.operationLogService.addOperationLog(fromAccount.getUser(), fromAccount, toAccount, fromAccount.getAccCode(),
                operationTransferRequest.getAmount().negate(), beforeSubAmount, afterSubAmount);
    }

    public ErrorStatus transfer(Account fromAccount, Account toAccount,
                                OperationTransferRequest operationTransferRequest) {
        ErrorStatus check = this.subMoney(fromAccount, toAccount, operationTransferRequest);
        if (check != ErrorStatus.OK) return check;

        check = this.addMoney(fromAccount, toAccount, fromAccount.getAccCode(), operationTransferRequest.getAmount());
        return check;
    }
}
