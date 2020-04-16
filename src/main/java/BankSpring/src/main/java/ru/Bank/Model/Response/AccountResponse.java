package ru.Bank.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.Bank.Entity.Account;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private UUID id;
    private BigDecimal amount;
    private String accCode;
    private Date createdAt;
    private Date updateAt;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.amount = account.getAmount();
        this.accCode = account.getAccCode();
        this.createdAt = Date.from(Instant.ofEpochSecond(account.getCreatedAt()));
        this.updateAt = Date.from(Instant.ofEpochSecond(account.getUpdatedAt()));
    }
}
