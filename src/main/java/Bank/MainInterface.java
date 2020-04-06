package Bank;

import Bank.Entity.Account;
import Bank.Entity.OperationLog;
import Bank.Entity.User;
import Bank.Service.AccountService;
import Bank.Service.Database.DatabaseConnector;
import Bank.Service.ErrorStatus.ErrorStatus;
import Bank.Service.OperationLogService;
import Bank.Service.UserService;
import Bank.Service.Utils.Currencies;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class MainInterface {
    private static final String configPath = "src/main/resources/config.json";
    private Scanner scanner;
    private User user;
    private UserService userService;
    private AccountService accountService;
    private OperationLogService operationLogService;
    private Connection connection;

    @SneakyThrows
    public MainInterface() {
        DatabaseConnector.runScript(configPath);
        this.connection = DatabaseConnector.getConnection(configPath);
        this.user = null;
        scanner = new Scanner(System.in);
        this.userService = new UserService(this.connection);
        this.accountService = new AccountService(this.connection);
        this.operationLogService = new OperationLogService(this.connection);
    }

    public void destroyConnection() throws SQLException {
        DatabaseConnector.destroyConnection(this.connection);
        this.userService.destroyConnection();
        this.accountService.destroyConnection();
        this.operationLogService.destroyConnection();
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public void login() throws SQLException {
        System.out.println("Введите логин или номер телефона без +7: ");
        String log = scanner.next();
        System.out.println("Введите пароль: ");
        String password = scanner.next();
        this.user = this.userService.login(log.toLowerCase(), password);
        if (this.user == null) {
            System.out.println("Неверный логин/пароль");
        }
    }

    public void registration() throws SQLException {
        System.out.println("Введите логин: ");
        String log = this.userService.checkEmptyData(scanner.next(), "логин");
        while (this.userService.checkExistUserByLogin(log)) {
            System.out.println("Данный логин уже используйте, введите другой логин: ");
            log = scanner.next();
        }
        System.out.println("Введите пароль: ");
        String password = this.userService.checkEmptyData(scanner.next(), "пароль");

        String cryptPassword = userService.cryptPassword(password);
        System.out.println("Введите номер телефона без +7: ");
        String phone = this.userService.checkEmptyData(scanner.next(), "телефон");
        while (this.userService.checkExistUserByPhone(phone)) {
            System.out.println("Данный номер телефона уже используется, введите другой: ");
            phone = scanner.next();
        }
        System.out.println("Введите адрес: ");
        String address = this.userService.checkEmptyData(scanner.next(), "адрес");

        this.user = new User(log.toLowerCase(), cryptPassword,  address, phone);
        int check = this.userService.registration(this.user);
        if (check != ErrorStatus.OK) {
            printErrorMessage(check);
            this.user = null;
        }
        else {
            this.user = this.userService.getUser(log, "");
            System.out.println("Регистрация прошла успешно");
        }
    }

    public void logout () {
        this.user = null;
    }

    public void myAccounts() throws SQLException {
        List<Account> accounts = this.accountService.getAllAccountsByUserId(this.user.getId());
        if (accounts == null || accounts.isEmpty()) {
            System.out.println("У вас пока нет ни одного счета");
            return;
        }
        printAllAccount(accounts);
    }

    public void createAccount() throws SQLException {
        Map<Integer, String> curr = Currencies.getMapCurrencies();
        printCurrency(curr);
        int accountCurrency = -1;
        while (!curr.containsKey(accountCurrency)) {
            System.out.println("Выберите валюту счета:");
            System.out.println("0 - вернуться в главное меню");
            if (scanner.hasNextInt()) {
                accountCurrency = scanner.nextInt();
            }
            else
                scanner.next();
            if (accountCurrency == 0) {
                return;
            }
        }
        Account account = new Account(this.user.getId(), curr.get(accountCurrency));
        int check = this.accountService.createAccount(account);
        if (check != ErrorStatus.OK) {
            printErrorMessage(check);
        } else {
            System.out.println("Счет успешно создан");
        }
    }

    public void addMoney() throws SQLException {
        Account account = choiceAccount();
        Map<Integer, String> curr = Currencies.getMapCurrencies();
        printCurrency(curr);
        int moneyCurrency = -1;
        while (!curr.containsKey(moneyCurrency)) {
            System.out.println("Выберите валюту пополнения:");
            System.out.println("0 - вернуться в главное меню");
            if (scanner.hasNextInt()) {
                moneyCurrency = scanner.nextInt();
            }
            else
                scanner.next();
            if (moneyCurrency == 0) {
                return;
            }
        }

        printDefaultExchangeValue();
        System.out.println("Введите сумму пополнения: ");
        if (scanner.hasNextBigDecimal()) {
            BigDecimal money = scanner.nextBigDecimal();
            int check = this.accountService.addMoney("", account.getId(), money, curr.get(moneyCurrency));
            if (check == ErrorStatus.OK) {
                System.out.println("Платеж успешно зачислен");
            } else {
                printErrorMessage(check);
            }
        }
        else
            System.out.println("Введена некорректная сумма");
    }

    public void transferAnotherUser() throws SQLException {
        System.out.println("Введите номер телефона пользователя: ");
        String phone = scanner.next();
        if (this.user.getPhone().equals(phone)) {
            System.out.println("Вы ввели свой собственный номер");
            return;
        }
        if (!this.userService.checkExistUserByPhone(phone)) {
            System.out.println("Такого пользователя не существует");
            return;
        }
        if (!this.accountService.checkExistAccountByPhone(phone)) {
            System.out.println("У этого пользователя еще нет счетов");
            return;
        }
        Account account = choiceAccount();
        printDefaultExchangeValue();
        System.out.println("Введите сумму перевода: ");
        if (scanner.hasNextBigDecimal()) {
            BigDecimal money = scanner.nextBigDecimal();
            if (money.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("Введена нулевая сумма");
                return;
            }
            int check = this.accountService.transfer(account.getId(), phone, money);
            if (check == ErrorStatus.OK) {
                System.out.println("Перевод успешно совершен");
            } else {
                printErrorMessage(check);
            }
        }
        else
            System.out.println("Введена некорректная сумма");
    }

    public void myOperations() throws SQLException {
        List<OperationLog> operationLogs = this.operationLogService.getAllOperationLogByUserId(this.user.getId());
        if (operationLogs == null || operationLogs.isEmpty()) {
            System.out.println("История операций пуста");
            return;
        }
        printAllOperationLogs(operationLogs);
    }

    public void printCurrency(Map<Integer, String> currencies) {
        System.out.println("Доступные валюты:");
        currencies.forEach((k, v) -> System.out.println(k + " - " + v));
        System.out.println("0 - вернуться в главное меню");
        System.out.println();
    }

    public Account choiceAccount() throws SQLException {
        List<Account> accounts = this.accountService.getAllAccountsByUserId(this.user.getId());
        if (accounts == null || accounts.isEmpty()) {
            System.out.println("У вас пока нет ни одного счета, чтобы пополнить/перевести средства");
            return null;
        }
        printAllAccount(accounts);
        int accountId = -1;
        while (accountId > accounts.size() || accountId <= 0) {
            if (accountId == 0) return null;
            System.out.println("Выберите счет: ");
            System.out.println("0 - вернуться в главное меню");
            if (scanner.hasNextInt()) {
                accountId = scanner.nextInt();
            }
            else
                scanner.next();
        }
        return accounts.get(accountId - 1);
    }

    public void printAllAccount(List<Account> accounts) {
        int i = 1;
        System.out.println("Ваши счета:");
        for(Account account: accounts) {
            System.out.println(i + " - " + account);
            i++;
        }
        System.out.println();
    }

    public void printAllOperationLogs(List<OperationLog> operationLogs) {
        int i = 1;
        System.out.println("История операций:");
        for(OperationLog operationLog: operationLogs) {
            System.out.println(i + " - " + operationLog);
            i++;
        }
        System.out.println();
    }

    public void printDefaultExchangeValue() {
        System.out.println("Курсы валют:");
        Currencies.defaultExchangeValue.forEach((k, v) -> System.out.println(k + ": " + v.setScale(2, RoundingMode.HALF_DOWN)));
    }

    public void printErrorMessage(int status) {
        System.out.println("Произошла ошибка в системе");
        System.out.println("Error code: " + status);
        System.out.println("Error message: " + ErrorStatus.getErrorMessage(status));
    }

    public void loginMenu() {
        System.out.println("1 - Войти в систему");
        System.out.println("2 - Зарегистрироваться");
        System.out.println("0 - Выйти");
        System.out.println();
    }

    public void userMenu() {
        System.out.println("1 - Мои счета");
        System.out.println("2 - Создать счет");
        System.out.println("3 - Пополнить свой счет");
        System.out.println("4 - Перевод денег другому пользователю");
        System.out.println("5 - Мои операции");
        System.out.println("9 - Выйти из системы");
        System.out.println("0 - Выйти");
        System.out.println();
    }

    public void mainMenu() throws SQLException{
        String command = "";
        while (!command.equals("0")) {
            if (this.user == null) {
                loginMenu();
                command = scanner.next();
                switch (command) {
                    case "1":
                        login();
                        break;
                    case "2":
                        registration();
                        break;
                    case "0":
                        DatabaseConnector.destroyConnection(this.connection);
                        break;
                }
            }
            else {
                userMenu();
                command = scanner.next();
                switch (command) {
                    case "1":
                        myAccounts();
                        break;
                    case "2":
                        createAccount();
                        break;
                    case "3":
                        addMoney();
                        break;
                    case "4":
                        transferAnotherUser();
                        break;
                    case "5":
                        myOperations();
                        break;
                    case "9":
                        logout();
                    case "0":
                        destroyConnection();
                        break;
                }
            }

        }
    }

    public static void main(String[] args) {
        try {
            MainInterface mainInterface = new MainInterface();
            mainInterface.mainMenu();
        } catch (SQLException |  NullPointerException | InputMismatchException e) {
            System.out.println("Произошла ошибка в работе системы");
//            e.printStackTrace();
        }
    }
}
