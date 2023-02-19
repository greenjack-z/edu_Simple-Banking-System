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

    public static boolean checkCardNumber(String number) {
        if (number.length() != 16) {
            return false;
        }
        if (!number.startsWith("400000")) {
            return false;
        }
        int checkSum = getCheckSum(number.substring(0, number.length() - 1));
        int lastChar = Character.getNumericValue(number.toCharArray()[15]);
        System.out.printf("sum: %d -- last: %d%n", checkSum, lastChar);
        return checkSum == lastChar;
    }

    public static boolean checkPIN(String pin) {
        return pin != null && pin.length() == 4;
    }



    record CheckResult(boolean result, String comment) {}
}
