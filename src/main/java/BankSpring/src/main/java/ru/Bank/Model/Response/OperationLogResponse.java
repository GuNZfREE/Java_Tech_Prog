package ru.Bank.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.Bank.Entity.Account;
import ru.Bank.Entity.OperationLog;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogResponse {
    private Integer id;
    private UUID fromAccount;
    private UUID toAccount;
    private String accCode;
    private BigDecimal amount;
    private BigDecimal beforeTransfer;
    private BigDecimal afterTransfer;
    private Date createdAt;
    private String type;

    public OperationLogResponse(OperationLog operationLog) {
        this.setId(operationLog.getId());
        this.setFromAccount(operationLog.getFromAccount() != null ? operationLog.getFromAccount().getId() : null);
        this.setToAccount(operationLog.getToAccount().getId());
        this.setAccCode(operationLog.getAccCode());
        this.setAmount(operationLog.getAmount());
        this.setBeforeTransfer(operationLog.getBeforeTransfer());
        this.setAfterTransfer(operationLog.getAfterTransfer());
        this.setCreatedAt(Date.from(Instant.ofEpochSecond(operationLog.getCreatedAt())));
        this.setType(operationLog.getFromAccount() != null ? "Транзакция" : "Пополнение счета");
    }
}
