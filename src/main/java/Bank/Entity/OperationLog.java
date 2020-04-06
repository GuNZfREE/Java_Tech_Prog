package Bank.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
public class OperationLog {
    private Integer id;
    private Integer userId;
    private String  fromAccountId;
    private String toAccountId;
    private String accCode;
    private BigDecimal amount;
    private BigDecimal beforeTransfer;
    private BigDecimal afterTransfer;
    private long createdAt;

    public OperationLog() {
        this.createdAt = Instant.now().getEpochSecond();
    }

    public OperationLog(int userId, String fromAccountId, String toAccountId, String accCode, BigDecimal amount,
                        BigDecimal beforeTransfer, BigDecimal afterTransfer) {
        this.setUserId(userId);
        this.setFromAccountId(fromAccountId);
        this.setToAccountId(toAccountId);
        this.setAccCode(accCode);
        this.setAmount(amount);
        this.setBeforeTransfer(beforeTransfer);
        this.setAfterTransfer(afterTransfer);
        this.setCreatedAt(Instant.now().getEpochSecond());
    }

    @Override
    public String toString() {
        String res = "№ " + this.getId() + ":";
        if (this.getFromAccountId() != null)
            res += " тип операции = перевод средств; счет отправителя = " + this.getFromAccountId() + ";";
        else
            res += " тип операции = пополнение баланса;";
        res += " счет получателя = " + this.getToAccountId() + "; валюта платежа = " + this.getAccCode() +
            "; сумма платежа = " + this.getAmount() + "; баланс до платежа = " + this.getBeforeTransfer() +
            "; баланс после платежа = " + this.getAfterTransfer() + "; дата выполнения: " +
                Date.from(Instant.ofEpochSecond(getCreatedAt()));
        return res;
    }
}
