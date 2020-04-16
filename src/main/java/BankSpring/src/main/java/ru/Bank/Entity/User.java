package ru.Bank.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
    private List<OperationLog> operations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Account> accounts;

    @Column(name = "created_at", nullable = false)
    private Long createdAt = Instant.now().getEpochSecond();

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "deleted_at")
    private Long deletedAt;


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
