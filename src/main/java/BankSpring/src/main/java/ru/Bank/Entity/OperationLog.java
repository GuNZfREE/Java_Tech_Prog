package ru.Bank.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.Bank.Model.Request.OperationTransferRequest;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "operationLog")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    @Column(name = "acc_code", nullable = false)
    private String accCode;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "before_transfer", nullable = false)
    private BigDecimal beforeTransfer;

    @Column(name = "after_transfer", nullable = false)
    private BigDecimal afterTransfer;

    @Column(name = "created_at", nullable = false)
    private Long createdAt = Instant.now().getEpochSecond();

    public OperationLog() {
        this.createdAt = Instant.now().getEpochSecond();
    }

    public OperationLog(User user, Account fromAccount, Account toAccount, String accCode, BigDecimal amount,
                        BigDecimal beforeTransfer, BigDecimal afterTransfer) {
        this.setUser(user);
        if (fromAccount != null)
            this.setFromAccount(fromAccount);
        this.setToAccount(toAccount);
        assert fromAccount != null;
        this.setAccCode(accCode);
        this.setAmount(amount);
        this.setBeforeTransfer(beforeTransfer);
        this.setAfterTransfer(afterTransfer);
        this.setCreatedAt(Instant.now().getEpochSecond());
    }

    @Override
    public String toString() {
        String res = "№ " + this.getId() + ":";
        if (this.getFromAccount() != null)
            res += " тип операции = перевод средств; счет отправителя = " + this.getFromAccount() + ";";
        else
            res += " тип операции = пополнение баланса;";
        res += " счет получателя = " + this.getToAccount() + "; валюта платежа = " + this.getAccCode() +
            "; сумма платежа = " + this.getAmount() + "; баланс до платежа = " + this.getBeforeTransfer() +
            "; баланс после платежа = " + this.getAfterTransfer() + "; дата выполнения: " +
                Date.from(Instant.ofEpochSecond(getCreatedAt()));
        return res;
    }
}
