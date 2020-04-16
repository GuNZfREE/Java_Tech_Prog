package ru.Bank.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.Bank.Entity.Account;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationTransferRequest {
    @NotNull(message = "Empty account id")
    UUID fromAccountId;

    @Pattern(regexp="^(0|[1-9][0-9]*)$")
    @NotEmpty(message = "Empty phone")
    String phone;

    @NotNull(message = "Empty amount")
    BigDecimal amount;

    public OperationTransferRequest(OperationAddOnAccountRequest operationAddOnAccountRequest) {
        this.amount = operationAddOnAccountRequest.getAmount();
    }

    public OperationTransferRequest(OperationAddByPhoneRequest operationAddByPhoneRequest) {
        this.phone = operationAddByPhoneRequest.getPhone();
        this.amount = operationAddByPhoneRequest.getAmount();
    }
}
