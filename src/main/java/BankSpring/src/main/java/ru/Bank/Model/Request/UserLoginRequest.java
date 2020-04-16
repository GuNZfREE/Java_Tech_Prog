package ru.Bank.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotEmpty(message = "Empty login")
    private String loginOrPhone;

    @NotEmpty(message = "Empty password")
    private String password;
}
