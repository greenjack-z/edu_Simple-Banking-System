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

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
