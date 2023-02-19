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

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
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
        var isValidNumber = AccountService.checkCardNumber(number);
        while (!isValidNumber.result()) {
            System.out.println(isValidNumber.comment());
            number = readInput();
            isValidNumber = AccountService.checkCardNumber(number);
        }
        if (!accounts.containsKey(number)) {
            System.out.println("wrong account number");
            return;
        }
        System.out.println("Enter your PIN");
        String pin = readInput();
        var isValidPin = AccountService.checkPIN(pin);
        int attempts = 0;
        while (currentAccount == null) {
            while (!isValidPin.result()) {
                System.out.println(AccountService.checkPIN(pin).comment());
                pin = readInput();
                isValidPin = AccountService.checkPIN(pin);

            }
            if (!accounts.get(number).pin.equals(pin)) {
                System.out.println("wrong pin for this number");
                attempts++;
            } else {
                currentAccount = accounts.get(number);
            }
            if (attempts == 3) {
                return;
            }
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