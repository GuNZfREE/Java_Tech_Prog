package Bank.Service.ErrorStatus;

public class ErrorStatus {
    public static final int OK = 1;
    public static final int DATABASE_ERROR = 2;

    public static final int ACCOUNT_NOT_EXIST = 21;
    public static final int ACCOUNT_NOT_ENOUGHT_AMOUNT = 22;
    public static final int ACCOUNT_CREATE_ERROR = 31;
    public static final int ACCOUNT_ADD_MONEY_ERROR = 32;
    public static final int ACCOUNT_SUB_MONEY_ERROR = 33;
    public static final int ACCOUNT_TRANSFER_MONEY_ERROR = 34;
    public static final int OPERATIONLOG_NOT_EXIST = 41;
    public static final int OPERATIONLOG_CREATE_ERROR = 51;
    public static final int LOGIN_ALREADY_EXIST = 61;
    public static final int PHONE_ALREADY_EXIST = 62;
    public static final int USER_ALREADY_EXIST = 63;
    public static final int USER_CREATE_ERROR = 71;
    public static final int USER_NOT_EXIST = 72;


    public static String getErrorMessage(int status) {
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
            case OPERATIONLOG_NOT_EXIST:
                return "Такой операции не существует";
            case OPERATIONLOG_CREATE_ERROR:
                return "Ошибка при создании истории платежа";
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
            default:
                return "Ошибка без обработки";
        }
    }
}
