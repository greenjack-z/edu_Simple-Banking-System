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

    public void insertCard(String number, String pin, int balance) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.setString(2, pin);
            statement.setInt(3, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered(String number) {
        String sql = "SELECT number FROM card";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(number)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPin (String number) {
        String sql = "SELECT number, pin FROM card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString("pin");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getBalance (String number) {
        String sql = "SELECT number, balance FROM card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("balance");
        } catch (SQLException e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public void updateBalance(String number, int balance) {
        String sql = "UPDATE card SET balance = ? WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, balance);
            statement.setString(2, number);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCard(String number) {
        String sql = "DELETE FROM card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
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
