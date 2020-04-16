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
import ru.Bank.Helper.Utils.Currencies;
import ru.Bank.Model.Request.OperationAddByPhoneRequest;
import ru.Bank.Model.Request.OperationAddOnAccountRequest;
import ru.Bank.Model.Request.OperationTransferRequest;
import ru.Bank.Model.Response.AbstractResponse;
import ru.Bank.Model.Response.MessageResponse;
import ru.Bank.Model.Response.ReturnResponse.ErrorReturnResponse;
import ru.Bank.Model.Response.ReturnResponse.MessageReturnResponse;
import ru.Bank.Service.AccountService;
import ru.Bank.Service.OperationLogService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Set;


@RestController
@RequestMapping("/api/operation")
public class OperationController extends AbstractController {

    private AccountService accountService;
    private OperationLogService operationLogService;

    @Autowired
    public OperationController(AccountService accountService, OperationLogService operationLogService) {
        this.accountService = accountService;
        this.operationLogService = operationLogService;
    }


    @ApiOperation(value = "Add amount on account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully", response = MessageReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    })
    @PostMapping("/add-on-account-by-id")
    public ResponseEntity addMoneyToAccount(@Valid @RequestBody OperationAddOnAccountRequest operationAddOnAccountRequest)
            throws  ValidationException {
        String username = this.getUsernameFromContext();
        operationAddOnAccountRequest.setAccCode(operationAddOnAccountRequest.getAccCode().toUpperCase());
        if (!this.accountService.checkCurrience(operationAddOnAccountRequest.getAccCode())) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.CURRENCE_NOT_EXIST);
        }
        User user = this.operationLogService.getUser(username);
        if (user == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);

        ErrorStatus check = this.operationLogService.checkMoney(operationAddOnAccountRequest.getAmount());
        if (check != ErrorStatus.OK)
            return this.errorResponse(HttpStatus.BAD_REQUEST, check);

        Account toAccount = this.accountService.getAccountByUuid(operationAddOnAccountRequest.getToAccountId());
        if (toAccount == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.OPERATION_TO_ACCOUNT_NOT_EXIST);

        check = accountService.addMoney(null, toAccount, operationAddOnAccountRequest.getAccCode(),
                operationAddOnAccountRequest.getAmount());
        if (check != ErrorStatus.OK) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, check);
        }

        return this.successResponse(new MessageResponse("Success"), HttpStatus.CREATED, ErrorStatus.OK);
    }


    @ApiOperation(value = "Add amount by phone")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully", response = MessageReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    })
    @PostMapping("/add-on-account-by-phone")
    public ResponseEntity addMoneyByPhone(@Valid @RequestBody OperationAddByPhoneRequest operationAddByPhoneRequest)
            throws  ValidationException {
        operationAddByPhoneRequest.setAccCode(operationAddByPhoneRequest.getAccCode().toUpperCase());
        if (!this.accountService.checkCurrience(operationAddByPhoneRequest.getAccCode())) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.CURRENCE_NOT_EXIST);
        }

        ErrorStatus check = this.operationLogService.checkMoney(operationAddByPhoneRequest.getAmount());
        if (check != ErrorStatus.OK)
            return this.errorResponse(HttpStatus.BAD_REQUEST, check);

        User toUser = this.operationLogService.getUser(operationAddByPhoneRequest.getPhone());
        if (toUser == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);

        Account toAccount = this.accountService.getAccountForOperation(toUser, operationAddByPhoneRequest.getAccCode(), null);
        if (toAccount == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_HAVE_ACCOUNTS);

        check = accountService.addMoney(null, toAccount, operationAddByPhoneRequest.getAccCode(),
                operationAddByPhoneRequest.getAmount());
        if (check != ErrorStatus.OK) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, check);
        }
        return this.successResponse(new MessageResponse("Success"), HttpStatus.CREATED, ErrorStatus.OK);
    }


    @ApiOperation(value = "transfer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully", response = MessageReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden",
                    response = ErrorReturnResponse.class),
    })
    @PostMapping("/transfer")
    public ResponseEntity transfer(@Valid @RequestBody OperationTransferRequest operationTransferRequest)
            throws  ValidationException {
        String username = this.getUsernameFromContext();
        User user = this.operationLogService.getUser(username);
        if (user == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);

        User toUser = this.operationLogService.getUser(operationTransferRequest.getPhone());
        if (toUser == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_EXIST);

        Account fromAccount = this.accountService.getAccountByUuid(operationTransferRequest.getFromAccountId());
        if (fromAccount == null)
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.OPERATION_FROM_ACCOUNT_NOT_EXIST);

        ErrorStatus check = this.accountService.checkOwn(fromAccount, user);
        if (check != ErrorStatus.OK) {
            return this.errorResponse(HttpStatus.FORBIDDEN, check);
        }

        check = this.operationLogService.checkMoney(operationTransferRequest.getAmount());
        if (check != ErrorStatus.OK)
            return this.errorResponse(HttpStatus.BAD_REQUEST, check);

        Account toAccount = this.accountService.getAccountForOperation(toUser, fromAccount.getAccCode(), fromAccount.getId());
        if (toAccount == null || toAccount.equals(fromAccount))
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.USER_NOT_HAVE_ACCOUNTS);

        check = accountService.transfer(fromAccount, toAccount, operationTransferRequest);
        if (check != ErrorStatus.OK) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, check);
        }
        return this.successResponse(new MessageResponse("Success"), HttpStatus.CREATED, ErrorStatus.OK);
    }


    @ApiOperation(value = "Get all Currencies", response = AbstractResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = AbstractResponse.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    })
    @GetMapping("/currencies-list")
    public ResponseEntity getCurrencies() {
        Set<String> currencies = Currencies.currencies;
        return this.successResponse(currencies, HttpStatus.OK, ErrorStatus.OK);
    }
}
