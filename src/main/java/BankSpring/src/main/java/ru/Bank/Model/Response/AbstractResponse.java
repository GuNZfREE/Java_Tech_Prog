package ru.Bank.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.Bank.Helper.Status.ErrorStatus;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractResponse<T> {
    T data;
    Date timestamp;
    int status;
    ErrorStatus errorCode;
    List<String> errors;
}
