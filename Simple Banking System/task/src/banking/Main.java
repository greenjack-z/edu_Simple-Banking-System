package banking;

import java.util.Scanner;

public class Main {
    Scanner scanner = new Scanner(System.in);
    Menu menu = new Menu(this);
    String currentCardNumber = null;
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
        while (dbService.isRegistered(number)) {
            number = AccountService.generateCardNumber();
        }
        String pin = AccountService.generatePin();
        dbService.insertCard(number, pin, 0);
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
            //error in input data format
            System.out.println("Wrong card number or PIN");
            return;
        }
        if (dbService.getPin(number) == null || !dbService.getPin(number).equals(pin)) {
            //account non exists or pin not correct
            System.out.println("Wrong card number or PIN");
            return;
        }
        System.out.println("You have successfully logged in!");
        currentCardNumber = number;
        menu.currentPage = Menu.Page.ACCOUNT;
    }

    public void logout() {
        System.out.println("You have successfully logged out!");
        currentCardNumber = null;
        menu.currentPage = Menu.Page.WELCOME;
    }

    public void balance() {
        System.out.printf("Balance: %d%n", dbService.getBalance(currentCardNumber));
    }
}