package ru.Bank.Controller.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Bank.Entity.Account;
import ru.Bank.Entity.OperationLog;
import ru.Bank.Entity.User;
import ru.Bank.Helper.Status.ErrorStatus;
import ru.Bank.Model.Request.AccountNewRequest;
import ru.Bank.Model.Response.AccountResponse;
import ru.Bank.Model.Response.OperationLogResponse;
import ru.Bank.Model.Response.ReturnResponse.AccountReturnResponse;
import ru.Bank.Model.Response.ReturnResponse.ErrorReturnResponse;
import ru.Bank.Model.Response.ReturnResponse.OperationLogReturnResponse;
import ru.Bank.Repository.AccountRepository;
import ru.Bank.Service.AccountService;
import ru.Bank.Service.OperationLogService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/account")
public class AccountController extends AbstractController {

    private AccountService accountService;
    private OperationLogService operationLogService;
    private AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountService accountService, AccountRepository accountRepository,
                             OperationLogService operationLogService) {
        this.accountService = accountService;
        this.operationLogService = operationLogService;
        this.accountRepository = accountRepository;
    }


    @ApiOperation(value = "Create new Account", response = AccountReturnResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created Account", response = AccountReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    })
    @PostMapping("/new")
    public ResponseEntity newAccount(@Valid @RequestBody AccountNewRequest accountNewRequest) throws  ValidationException {
        String username = this.getUsernameFromContext();
        accountNewRequest.setAccCode(accountNewRequest.getAccCode().toUpperCase());
        if (!this.accountService.checkCurrience(accountNewRequest.getAccCode())) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.CURRENCE_NOT_EXIST);
        }
        User user = this.accountService.getUser(username);
        if (user == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);
        Account newAccount = new Account(user, accountNewRequest.getAccCode());
        newAccount = this.accountRepository.save(newAccount);
        if (newAccount == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.ACCOUNT_CREATE_ERROR);
        return this.successResponse(new AccountResponse(newAccount),
                HttpStatus.CREATED, ErrorStatus.OK);
    }


    @ApiOperation(value = "Get all user's Account", response = AccountReturnResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully",
                    response = AccountReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    })
    @GetMapping("/my-accounts")
    public ResponseEntity getAccount() {
        String username = this.getUsernameFromContext();
        User user = this.accountService.getUser(username);
        if (user == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);

        List<Account> accounts = accountService.getAccountsByUser(user);
        List<AccountResponse> responses = accounts
                .stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
        return this.successResponse(responses, HttpStatus.OK, ErrorStatus.OK);
    }


    @ApiOperation(value = "Get all user's Operations Log",
            response = OperationLogReturnResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully",
                    response = OperationLogReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    })
    @GetMapping("/my-all-history")
    public ResponseEntity getAllHistory() {
        String username = this.getUsernameFromContext();
        User user = this.accountService.getUser(username);
        if (user == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);

        List<OperationLog> operationLogs = operationLogService.getHistoryByUser(user);
        List<OperationLogResponse> responses = operationLogs
                .stream()
                .map(OperationLogResponse::new)
                .collect(Collectors.toList());
        return this.successResponse(responses, HttpStatus.OK, ErrorStatus.OK);
    }

    @ApiOperation(value = "Get accounts Operations Log",
            response = OperationLogReturnResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully",
                    response = OperationLogReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden",
                    response = ErrorReturnResponse.class),
    })
    @GetMapping("/my-history-by-account/{uuid}")
    public ResponseEntity getHistoryByAccount(@PathVariable("uuid") UUID uuid) {
        String username = this.getUsernameFromContext();
        User user = this.accountService.getUser(username);
        if (user == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);

        Account account = accountService.getAccountByUuid(uuid);
        if (account == null) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.ACCOUNT_NOT_EXIST);
        }

        ErrorStatus check = this.accountService.checkOwn(account, user);
        if (check != ErrorStatus.OK) {
            return this.errorResponse(HttpStatus.FORBIDDEN, check);
        }

        List<OperationLog> operationLogs = operationLogService.getHistoryByAccount(account);
        List<OperationLogResponse> responses = operationLogs
                .stream()
                .map(OperationLogResponse::new)
                .collect(Collectors.toList());
        return this.successResponse(responses, HttpStatus.OK, ErrorStatus.OK);
    }
}
