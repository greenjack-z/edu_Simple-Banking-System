package banking;

import java.security.SecureRandom;

public class AccountService {
    static final SecureRandom secureRandom = new SecureRandom();

    public static String generatePin() {
        return "%04d".formatted(secureRandom.nextInt(10000));
    }

    public static String generateCardNumber() {
        final String BIN = "400000";
        String accountNumber = "%09d".formatted(secureRandom.nextInt(100000000));
        String cardNumber = BIN+accountNumber;
        int checksum = getCheckSum(cardNumber);
        return cardNumber + checksum;
    }

    private static int getCheckSum(String number) {
        int[] digits = number.chars().map(Character::getNumericValue).toArray();
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            if (i % 2 == 0) {
                int digit = digits[i] * 2;
                sum += digit > 9 ? digit - 9 : digit;
            } else {
                sum += digits[i];
            }
        }
        return sum % 10 == 0 ? 0 : 10 - sum % 10;
    }

    public static CheckResult checkCardNumber(String number) {
        if (number == null) {
            return new CheckResult(false, "card number is null");
        }
        if (number.length() != 16) {
            return new CheckResult(false, "card number have to be 16 digits");
        }
        if (!number.startsWith("400000")) {
            return new CheckResult(false, "wrong BIN");
        }
        int checkSum = getCheckSum(number.substring(0, number.length() - 1));
        int lastChar = Character.getNumericValue(number.toCharArray()[15]);
        System.out.printf("sum: %d -- last: %d%n", checkSum, lastChar);
        if (checkSum != lastChar) {
            return new CheckResult(false, "Wrong card number");
        }
        return new CheckResult(true, "card number is ok");
    }

    public static CheckResult checkPIN(String pin) {
        if (pin == null) {
            return new CheckResult(false, "pin is null");
        }
        if (pin.length() != 4) {
            return new CheckResult(false, "pin have to be 4 digits");
        }
        return new CheckResult(true, "pin is ok");
    }

    record CheckResult(boolean result, String comment) {}
}
