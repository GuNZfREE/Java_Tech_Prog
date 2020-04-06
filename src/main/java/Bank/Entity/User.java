package Bank.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String login;
    private String password;
    private String address;
    private String phone;
    private long createdAt;

    public User() {
        this.createdAt = Instant.now().getEpochSecond();
    }

    public User(String login, String password, String address, String phone) {
        this.setLogin(login);
        this.setPassword(password);
        this.setAddress(address);
        this.setPhone(phone);
        this.setCreatedAt(Instant.now().getEpochSecond());
    }
}
