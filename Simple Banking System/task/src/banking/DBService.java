package banking;

import java.sql.*;

public class DBService {
    Connection connection = null;
    public void connectToDatabase(String fileName) {
        try {
            String url = "jdbc:sqlite:" + fileName;
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS card (
                    id INTEGER PRIMARY KEY,
                    number TEXT NOT NULL,
                    pin TEXT NOT NULL,
                    balance INTEGER DEFAULT 0
                );
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert (Account account) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.number);
            statement.setString(2, account.pin);
            statement.setInt(3, account.balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccountFromDB (String number) {
        String sql = "SELECT number, pin, balance FROM card WHERE number = ?";
        Account account = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            account = new Account(number, resultSet.getString("pin"), resultSet.getInt("balance"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
