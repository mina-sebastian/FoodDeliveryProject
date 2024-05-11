package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.person.User;
import me.mina.fooddeli.model.person.PremiumUser;
import me.mina.fooddeli.model.person.UserInfo;
import me.mina.fooddeli.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.mina.fooddeli.utils.Constants.USERS_TABLE;

public class UserDao implements Dao<User> {

    public UserDao() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "password VARCHAR(255)," +
                "userInfo TEXT," +
                "discountRate DOUBLE" + // Only used for PremiumUser
                ");";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Checked/created the table 'users' successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> get(int id) {
        String sql = "SELECT * FROM " + USERS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getDouble("discountRate") > 0
                        ? new PremiumUser(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        getUserInfoFromString(resultSet.getString("userInfo")),
                        resultSet.getDouble("discountRate"))
                        : new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        getUserInfoFromString(resultSet.getString("userInfo")))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM " + USERS_TABLE;
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getDouble("discountRate") > 0
                        ? new PremiumUser(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        getUserInfoFromString(resultSet.getString("userInfo")),
                        resultSet.getDouble("discountRate"))
                        : new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        getUserInfoFromString(resultSet.getString("userInfo")))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public int create(User user) {
        String sql = "INSERT INTO " + USERS_TABLE + " (name, password, userInfo, discountRate) VALUES (?, ?, ?, ?)";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, userInfoToString(user.getUserInfo()));
            double discountRate = user instanceof PremiumUser ? ((PremiumUser) user).getDiscountRate() : 0;
            statement.setDouble(4, discountRate);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    return user.getId();
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void update(int id, User user) {
        String sql = "UPDATE " + USERS_TABLE + " SET name = ?, password = ?, userInfo = ?, discountRate = ? WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, userInfoToString(user.getUserInfo()));
            double discountRate = user instanceof PremiumUser ? ((PremiumUser) user).getDiscountRate() : 0;
            statement.setDouble(4, discountRate);
            statement.setInt(5, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update error: User with id " + id + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM " + USERS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Delete error: User with id " + user.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private UserInfo getUserInfoFromString(String userInfoStr) {
        String[] parts = userInfoStr.split(",");
        return new UserInfo(parts[0], parts[1]);
    }

    private String userInfoToString(UserInfo userInfo) {
        return userInfo.getAddress() + "," + userInfo.getPhoneNumber();
    }
}
