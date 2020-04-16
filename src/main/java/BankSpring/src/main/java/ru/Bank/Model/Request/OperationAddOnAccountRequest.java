package ru.Bank.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationAddOnAccountRequest {
    @NotNull(message = "Empty account id")
    UUID toAccountId;

    @NotEmpty(message = "Empty accCode")
    String accCode;

    @NotNull(message = "Empty amount")
    BigDecimal amount;
}
