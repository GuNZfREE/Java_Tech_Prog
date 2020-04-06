package Bank.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class Account {
    private String id;
    private Integer userId;
    private BigDecimal amount = new BigDecimal(0.0);
    private String accCode;
    private long createdAt;
    private long deletedAt;


    public Account() {
        this.createdAt = Instant.now().getEpochSecond();
    }


    public Account(int userId, String accCode){
        this.setUserId(userId);
        this.setAccCode(accCode);
        this.createdAt = Instant.now().getEpochSecond();
    }

    @Override
    public String toString() {
        return "№ " + this.getId() + ": " +
                "валюта - " + this.getAccCode() + ", " +
                "баланс = " + this.getAmount();
    }
}
