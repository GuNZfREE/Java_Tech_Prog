package ru.Bank.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountNewRequest {
    @NotEmpty(message = "Empty accCode")
    String accCode;
}
