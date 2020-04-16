package ru.Bank.Controller.Api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.Bank.Helper.Status.ErrorStatus;
import ru.Bank.Model.Response.AbstractResponse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractController {
    public String getUsernameFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    public ResponseEntity successResponse(Object object, HttpStatus status, ErrorStatus errorCode) {
        AbstractResponse abstractResponse = new AbstractResponse<>(object, new Date(), status.value(), errorCode, null);
        return new ResponseEntity<>(abstractResponse, status);
    }

    public ResponseEntity errorResponse(HttpStatus status, ErrorStatus errorCode) {
        List<String> errors = Stream.of(ErrorStatus.getErrorMessage(errorCode))
                .collect(Collectors.toList());
        AbstractResponse abstractResponse = new AbstractResponse<>(null, new Date(), status.value(), errorCode, errors);
        return new ResponseEntity<>(abstractResponse, status);
    }
}
