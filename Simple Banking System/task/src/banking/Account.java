package banking;

public class Account {
    String number;
    String pin;
    int balance;

    public Account(String number, String pin) {
        this.number = number;
        this.pin = pin;
        this.balance = 0;
    }
}
