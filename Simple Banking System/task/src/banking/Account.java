package banking;

public class Account {
    String number;
    String pin;
    int balance;

    public Account(String number, String pin, int balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }
}
