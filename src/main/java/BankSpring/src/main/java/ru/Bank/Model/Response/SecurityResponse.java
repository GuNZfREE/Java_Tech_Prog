package ru.Bank.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.Bank.Entity.Account;
import ru.Bank.Entity.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityResponse {
    private String token;
    private int userId;
    private String login;
    private String phone;

    public SecurityResponse(String token, User user) {
        this.token = token;
        this.userId = user.getId();
        this.login = user.getLogin();
        this.phone = user.getPhone();
    }
}
