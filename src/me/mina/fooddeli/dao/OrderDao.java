package me.mina.fooddeli.dao;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.order.OrderStatus;
import me.mina.fooddeli.model.person.UserInfo;
import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.mina.fooddeli.utils.Constants.ORDERS_TABLE;

public class OrderDao implements Dao<Order> {

    public OrderDao() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS "+ORDERS_TABLE+" (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_info TEXT," +
                "status TEXT," +
                "items TEXT" +
                ");";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Checked/created the table 'orders' successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Order> get(int id) {
        String sql = "SELECT * FROM " + ORDERS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDERS_TABLE + ";";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(createOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public int create(Order order) {
        String sql = "INSERT INTO " + ORDERS_TABLE + " (user_info, status, items) VALUES (?, ?, ?)";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        for(OrderItem orderItem : order.getItems()){
            if(orderItem.getId() < 1 || FoodDeliveryService.getOrderItemDao().get(orderItem.getId()).isEmpty()){
                int orderItemId = FoodDeliveryService.getOrderItemDao().create(orderItem);
                orderItem.setId(orderItemId);
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, userInfoToString(order.getUserInfo()));
            statement.setString(2, order.getStatus().name());
            statement.setString(3, joinOrderItemIds(order.getItems()));
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                    return order.getId();
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void update(int id, Order order) {
        String sql = "UPDATE " + ORDERS_TABLE + " SET user_info = ?, status = ?, items = ? WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        for(OrderItem orderItem : order.getItems()){
            if(orderItem.getId() < 1 || FoodDeliveryService.getOrderItemDao().get(orderItem.getId()).isEmpty()){
                int orderItemId = FoodDeliveryService.getOrderItemDao().create(orderItem);
                orderItem.setId(orderItemId);
            }else{
                FoodDeliveryService.getOrderItemDao().update(orderItem.getId(), orderItem);
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userInfoToString(order.getUserInfo()));
            statement.setString(2, order.getStatus().name());
            statement.setString(3, joinOrderItemIds(order.getItems()));
            statement.setInt(4, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update error: Order with id " + id + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Order order) {
        String sql = "DELETE FROM " + ORDERS_TABLE + " WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, order.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Delete error: Order with id " + order.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order createOrderFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String userInfoStr = resultSet.getString("user_info");
        String status = resultSet.getString("status");
        UserInfo userInfo = getUserInfoFromString(userInfoStr);
        List<OrderItem> orderItems = new ArrayList<>();
        int[] orderItemIds = Arrays.stream(resultSet.getString("items").split(",")).mapToInt(Integer::parseInt).toArray();
        for (int orderItemId : orderItemIds) {
            FoodDeliveryService.getOrderItemDao().get(orderItemId).ifPresent(orderItems::add);
        }
        return new Order(id, userInfo, orderItems, OrderStatus.valueOf(status));
    }

    private UserInfo getUserInfoFromString(String userInfo) {
        String[] parts = userInfo.split(",");
        return new UserInfo(parts[0], parts[1]);
    }

    private String userInfoToString(UserInfo userInfo) {
        return userInfo.getAddress()+ "," + userInfo.getPhoneNumber();
    }



    private String joinOrderItemIds(List<OrderItem> orderItems) {
        return orderItems.stream().map(oi -> Integer.toString(oi.getId())).collect(Collectors.joining(","));
    }

}
