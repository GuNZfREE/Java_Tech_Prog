package ru.Bank.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequest {
    @NotEmpty(message = "Empty login")
    private String login;

    @NotEmpty(message = "Empty password")
    private String password;

    @Pattern(regexp="^(0|[1-9][0-9]*)$")
    @NotEmpty(message = "Empty phone")
    private String phone;

    @NotEmpty(message = "Empty address")
    private String address;
}
