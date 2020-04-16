package ru.Bank.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationAddByPhoneRequest {
    @Pattern(regexp="^(0|[1-9][0-9]*)$")
    @NotEmpty(message = "Empty phone")
    String phone;

    @NotEmpty(message = "Empty accCode")
    String accCode;

    @NotNull(message = "Empty amount")
    BigDecimal amount;
}
