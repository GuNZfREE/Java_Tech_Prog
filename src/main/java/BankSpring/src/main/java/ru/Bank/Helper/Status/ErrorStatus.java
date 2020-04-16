package ru.Bank.Helper.Status;

public enum ErrorStatus {
    OK, 
    DATABASE_ERROR,
    INVALID_CREDENTIALS,
    INVALID_TOKEN,
    VALIDATE_ERROR,
    EXCEPTION_ERROR,

    ACCOUNT_NOT_EXIST,
    ACCOUNT_NOT_ENOUGHT_AMOUNT,
    ACCOUNT_CREATE_ERROR,
    ACCOUNT_ADD_MONEY_ERROR,
    ACCOUNT_SUB_MONEY_ERROR,
    ACCOUNT_TRANSFER_MONEY_ERROR,
    ACCOUNT_NOT_OWN,

    OPERATIONLOG_NOT_EXIST,
    OPERATIONLOG_CREATE_ERROR,
    OPERATION_INCORRECT_AMOUNT,
    OPERATION_TO_ACCOUNT_NOT_EXIST,
    OPERATION_FROM_ACCOUNT_NOT_EXIST,

    LOGIN_ALREADY_EXIST,
    PHONE_ALREADY_EXIST,
    LOGIN_INVALID,

    USER_ALREADY_EXIST,
    USER_CREATE_ERROR,
    USER_NOT_EXIST,
    USER_NOT_HAVE_ACCOUNTS,

    CURRENCE_NOT_EXIST;


    public static String getErrorMessage(ErrorStatus status) {
        switch (status) {
            case DATABASE_ERROR:
                return "Ошибка в подключении к БД";
            case ACCOUNT_NOT_EXIST:
                return "Счета не существует";
            case ACCOUNT_NOT_ENOUGHT_AMOUNT:
                return "На счету недостаточно средств";
            case ACCOUNT_CREATE_ERROR:
                return "Ошибка при создании счета";
            case ACCOUNT_ADD_MONEY_ERROR:
                return "Ошибка при добавлении средств";
            case ACCOUNT_SUB_MONEY_ERROR:
                return "Ошибка при снятии средств";
            case ACCOUNT_TRANSFER_MONEY_ERROR:
                return "Ошибка при оформлении перевода";
            case ACCOUNT_NOT_OWN:
                return "Не владелец счета";
            case OPERATIONLOG_NOT_EXIST:
                return "Такой операции не существует";
            case OPERATIONLOG_CREATE_ERROR:
                return "Ошибка при создании истории платежа";
            case OPERATION_TO_ACCOUNT_NOT_EXIST:
                return "Счета не существует";
            case OPERATION_FROM_ACCOUNT_NOT_EXIST:
                return "Счета не существует";
            case OPERATION_INCORRECT_AMOUNT:
                return "Неккоретная сумма";
            case LOGIN_ALREADY_EXIST:
                return "Логин уже используется";
            case PHONE_ALREADY_EXIST:
                return "Номер уже используется";
            case USER_ALREADY_EXIST:
                return "Такой пользователь уже существует";
            case USER_CREATE_ERROR:
                return "Ошибка при создании пользователя";
            case USER_NOT_EXIST:
                return "Пользователя не существует";
            case CURRENCE_NOT_EXIST:
                return "Такой валюты не существует";
            case USER_NOT_HAVE_ACCOUNTS:
                return "У пользователя нет еще ни одного счета";
            case LOGIN_INVALID:
                return "Неверное имя пользователя или пароль";
            case INVALID_TOKEN:
                return "Неверный токен";
            default:
                return "Ошибка без обработки";
        }
    }
}
