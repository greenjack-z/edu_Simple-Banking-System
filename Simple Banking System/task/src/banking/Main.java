package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    Scanner scanner = new Scanner(System.in);
    Map<String, Account> accounts = new HashMap<>();
    Menu menu = new Menu(this);
    Account currentAccount = null;
    boolean exit = false;

    DBService dbService = new DBService();

    public static void main(String[] args) {
        String fileName = parseFileName(args);
        new Main().run(fileName);
    }

    private static String parseFileName(String[] args) {
        boolean fileNameArg = false;
        for (String arg : args) {
            if (fileNameArg) {
                return arg;
            }
            if (arg.equalsIgnoreCase("-fileName")) {
                fileNameArg = true;
            }
        }
        return "card.s3db";
    }

    private void run(String fileName) {
        dbService.connectToDatabase(fileName);
        dbService.createTable();
        while (!exit) {
            menu.print();
            try {
                String input = readInput();
                menu.choose(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.out.println("please enter the number!");
            }
        }
        scanner.close();
        dbService.closeConnection();
    }

    private String readInput() {
        return scanner.nextLine();
    }

    public void exit() {
        System.out.println("Bye!");
        exit = true;
    }

    public void createAccount() {
        String number = AccountService.generateCardNumber();
        while (accounts.containsKey(number)) {
            number = AccountService.generateCardNumber();
        }
        String pin = AccountService.generatePin();
        Account account = new Account(number, pin);
        accounts.put(number, account);
        dbService.insert(account);
        System.out.printf("""
                Your card has been created
                Your card number:
                %s
                Your card PIN:
                %s
                """, number, pin);
    }

    public void login() {
        System.out.println("Enter your card number:");
        String number = readInput();
        System.out.println("Enter your PIN");
        String pin = readInput();
        if (!AccountService.checkCardNumber(number) || !AccountService.checkPIN(pin)) {
            System.out.println("Wrong card number or PIN");
            return;
        }
        System.out.println("You have successfully logged in!");
        menu.currentPage = Menu.Page.ACCOUNT;
    }

    public void logout() {
        System.out.println("You have successfully logged out!");
        currentAccount = null;
        menu.currentPage = Menu.Page.WELCOME;
    }

    public void balance() {
        System.out.printf("Balance: %d%n", currentAccount.balance);
    }
}