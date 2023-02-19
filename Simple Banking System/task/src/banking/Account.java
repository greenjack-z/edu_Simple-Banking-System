package banking;

public class Account {
    String number;
    String pin;
    long balance;

    public Account(String number, String pin) {
        this.number = number;
        this.pin = pin;
        this.balance = 0L;
    }
}
