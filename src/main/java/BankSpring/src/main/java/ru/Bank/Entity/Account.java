package ru.Bank.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;
import ru.Bank.EventListener.AccountListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
@EntityListeners(AccountListener.class)
@Getter
@Setter
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(36)", unique = true, nullable = false)
    private UUID id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount = new BigDecimal(0.0);

    @OneToMany(mappedBy = "fromAccount", fetch=FetchType.LAZY)
    private List<OperationLog> operationsFrom;

    @OneToMany(mappedBy = "toAccount", fetch=FetchType.LAZY)
    private List<OperationLog> operationsTo;

    @Column(name = "acc_code", nullable = false)
    private String accCode;

    @Column(name = "created_at", nullable = false)
    private Long createdAt = Instant.now().getEpochSecond();

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "deleted_at")
    private Long deletedAt;


    public Account() {
        this.createdAt = Instant.now().getEpochSecond();
    }

    public Account(User user, String accCode){
        this.setUser(user);
        this.setAccCode(accCode);
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = Instant.now().getEpochSecond();
    }

    @Override
    public String toString() {
        return "№ " + this.getId() + ": " +
                "валюта - " + this.getAccCode() + ", " +
                "баланс = " + this.getAmount();
    }
}
