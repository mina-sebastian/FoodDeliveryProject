package me.mina.fooddeli.dao;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.mina.fooddeli.utils.Constants.ORDER_ITEMS_TABLE;

public class OrderItemDao implements Dao<OrderItem> {

    public OrderItemDao() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + ORDER_ITEMS_TABLE + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "menu_item_id INT," +
                "quantity INT" +
                ");";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Checked/created the table 'order_items' successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<OrderItem> get(int id) {
        String sql = "SELECT * FROM " + ORDER_ITEMS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createOrderItemFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<OrderItem> getAll() {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDER_ITEMS_TABLE;
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderItems.add(createOrderItemFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    @Override
    public int create(OrderItem orderItem) {
        String sql = "INSERT INTO " + ORDER_ITEMS_TABLE + " (menu_item_id, quantity) VALUES (?, ?)";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (orderItem.getItem().getId() < 1 || FoodDeliveryService.getMenuItemDao().get(orderItem.getItem().getId()).isEmpty()) {
            int newMenuItemId = FoodDeliveryService.getMenuItemDao().create(orderItem.getItem());
            orderItem.getItem().setId(newMenuItemId);
        }
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, orderItem.getItem().getId());
            statement.setInt(2, orderItem.getQuantity());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order item failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderItem.setId(generatedKeys.getInt(1));
                    return orderItem.getId();
                } else {
                    throw new SQLException("Creating order item failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void update(int id, OrderItem orderItem) {
        String sql = "UPDATE " + ORDER_ITEMS_TABLE + " SET menu_item_id = ?, quantity = ? WHERE id = ?";
        if(orderItem.getItem().getId() < 1 || FoodDeliveryService.getMenuItemDao().get(orderItem.getItem().getId()).isEmpty())
            FoodDeliveryService.getMenuItemDao().create(orderItem.getItem());
        else if(FoodDeliveryService.getMenuItemDao().get(orderItem.getItem().getId()).isPresent()) {
            FoodDeliveryService.getMenuItemDao().update(orderItem.getItem().getId(), orderItem.getItem());
        }
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderItem.getItem().getId());
            statement.setInt(2, orderItem.getQuantity());
            statement.setInt(3, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update error: Order item with id " + id + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(OrderItem orderItem) {
        String sql = "DELETE FROM " + ORDER_ITEMS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderItem.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Delete error: Order item with id " + orderItem.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private OrderItem createOrderItemFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int menuItemId = resultSet.getInt("menu_item_id");
        int quantity = resultSet.getInt("quantity");
        MenuItem item = FoodDeliveryService.getMenuItemDao().get(menuItemId).orElse(null);
        return new OrderItem(id, item, quantity);
    }
}
