package ru.Bank.Helper.ExceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.Bank.Helper.Status.ErrorStatus;
import ru.Bank.Model.Response.AbstractResponse;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        AbstractResponse abstractResponse = new AbstractResponse<>(null, new Date(), status.value(),
                ErrorStatus.VALIDATE_ERROR, errors);
        return new ResponseEntity<>(abstractResponse, headers, status);
    }

    @ExceptionHandler({ Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        List<String> errors = Stream.of(ex.getMessage())
                .collect(Collectors.toList());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        AbstractResponse abstractResponse = new AbstractResponse<>(null, new Date(), status.value(),
                ErrorStatus.EXCEPTION_ERROR, errors);
        return new ResponseEntity<>(abstractResponse, new HttpHeaders(), status);
    }
}
