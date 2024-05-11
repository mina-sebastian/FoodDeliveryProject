package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.mina.fooddeli.utils.Constants.MENU_ITEMS_TABLE;

public class MenuItemDao implements Dao<MenuItem> {

    public MenuItemDao() {
        createIfNotExists();
    }

    private void createIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + MENU_ITEMS_TABLE + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "price DECIMAL(10, 2) NOT NULL," +
                "ingredients TEXT" +
                ");";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Checked/created the table 'menu_items' successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<MenuItem> get(int id) {
        String sql = "SELECT * FROM " + MENU_ITEMS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createMenuItemFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<MenuItem> getAll() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM " + MENU_ITEMS_TABLE;
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                menuItems.add(createMenuItemFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public int create(MenuItem menuItem) {
        String sql = "INSERT INTO " + MENU_ITEMS_TABLE + " (name, price, ingredients) VALUES (?, ?, ?)";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, menuItem.getName());
            statement.setDouble(2, menuItem.getPrice());
            statement.setString(3, String.join(",", menuItem.getIngredients())); // Assumes ingredients are stored as a comma-separated string
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Consider better error handling
    }

    @Override
    public void update(int id, MenuItem menuItem) {
        String sql = "UPDATE " + MENU_ITEMS_TABLE + " SET name = ?, price = ?, ingredients = ? WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, menuItem.getName());
            statement.setDouble(2, menuItem.getPrice());
            statement.setString(3, String.join(",", menuItem.getIngredients()));
            statement.setInt(4, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update error: The menuItem with id " + id + " does not exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MenuItem menuItem) {
        String sql = "DELETE FROM " + MENU_ITEMS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuItem.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private MenuItem createMenuItemFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        List<String> ingredients = List.of(resultSet.getString("ingredients").split(","));
        return new MenuItem(id, name, price, ingredients);
    }
}
