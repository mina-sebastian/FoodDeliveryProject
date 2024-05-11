package me.mina.fooddeli.dao;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.model.restaurant.Restaurant;
import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static me.mina.fooddeli.utils.Constants.RESTAURANTS_TABLE;
import static me.mina.fooddeli.utils.Utils.reviewsToRating;

public class RestaurantDao implements Dao<Restaurant> {

    public RestaurantDao() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS " + RESTAURANTS_TABLE + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "menu TEXT," +
                "reviews TEXT," +
                "queue TEXT" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Checked/created the table 'restaurants' successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Restaurant> get(int id) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT * FROM " + RESTAURANTS_TABLE + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createRestaurantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Restaurant> getAll() {
        List<Restaurant> restaurants = new ArrayList<>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT * FROM " + RESTAURANTS_TABLE;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                restaurants.add(createRestaurantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    @Override
    public int create(Restaurant restaurant) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO " + RESTAURANTS_TABLE + " (name, menu, reviews, queue) VALUES (?, ?, ?, ?)";
        for(MenuItem menuItem : restaurant.getMenu()){
            if(menuItem.getId() < 1 || FoodDeliveryService.getMenuItemDao().get(menuItem.getId()).isEmpty()) {
                int menuItemId = FoodDeliveryService.getMenuItemDao().create(menuItem);
                menuItem.setId(menuItemId);
            }
        }
        for(Review review : restaurant.getReviews()){
            if(review.getId() < 1 || FoodDeliveryService.getReviewDao().get(review.getId()).isEmpty()) {
                int reviewId = FoodDeliveryService.getReviewDao().create(review);
                review.setId(reviewId);
            }
        }
        for(Order order : restaurant.getQueue()){
            if(order.getId() < 1 || FoodDeliveryService.getOrderRepositoryService().get(order.getId()).isEmpty()) {
                int orderId = FoodDeliveryService.getOrderRepositoryService().create(order, restaurant);
                order.setId(orderId);
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, restaurant.getName());
            statement.setString(2, joinMenuItemIds(restaurant.getMenu()));
            statement.setString(3, joinReviewIds(restaurant.getReviews()));
            statement.setString(4, joinQueueIds(restaurant.getQueue()));
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating restaurant failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    restaurant.setId(generatedKeys.getInt(1));
                    return restaurant.getId();
                } else {
                    throw new SQLException("Creating restaurant failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void update(int id, Restaurant restaurant) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String sql = "UPDATE " + RESTAURANTS_TABLE + " SET name = ?, menu = ?, reviews = ?, queue = ? WHERE id = ?";
        for(MenuItem menuItem : restaurant.getMenu()){
            if(menuItem.getId() < 1 || FoodDeliveryService.getMenuItemDao().get(menuItem.getId()).isEmpty()){
                int menuItemId = FoodDeliveryService.getMenuItemDao().create(menuItem);
                menuItem.setId(menuItemId);
            }else{
                FoodDeliveryService.getMenuItemDao().update(menuItem.getId(), menuItem);
            }
        }
        for(Review review : restaurant.getReviews()){
            if(review.getId() < 1 || FoodDeliveryService.getReviewDao().get(review.getId()).isEmpty()){
                int reviewId = FoodDeliveryService.getReviewDao().create(review);
                review.setId(reviewId);
            }else{
                FoodDeliveryService.getReviewDao().update(review.getId(), review);
            }
        }
        for(Order order : restaurant.getQueue()){
            if(order.getId() < 1 || FoodDeliveryService.getOrderRepositoryService().get(order.getId()).isEmpty()){
                int orderId = FoodDeliveryService.getOrderRepositoryService().create(order, restaurant);
                order.setId(orderId);
            }else{
                FoodDeliveryService.getOrderRepositoryService().update(order.getId(), order);
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, restaurant.getName());
            statement.setString(2, joinMenuItemIds(restaurant.getMenu()));
            statement.setString(3, joinReviewIds(restaurant.getReviews()));
            statement.setString(4, joinQueueIds(restaurant.getQueue()));
            statement.setInt(5, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update error: Restaurant with id " + id + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Restaurant restaurant) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String sql = "DELETE FROM " + RESTAURANTS_TABLE + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurant.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Delete error: Restaurant with id " + restaurant.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Restaurant createRestaurantFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        List<MenuItem> menuItems = new ArrayList<>();
        if(!resultSet.getString("menu").isEmpty()){
            int[] menuIDs = Arrays.stream(resultSet.getString("menu").split(",")).mapToInt(Integer::parseInt).toArray();
            for(int menuID : menuIDs) {
                FoodDeliveryService.getMenuItemDao().get(menuID).ifPresent(menuItems::add);
            }
        }
        List<Review> reviews = new ArrayList<>();
        if(!resultSet.getString("reviews").isEmpty()){
            int[] reviewIds = Arrays.stream(resultSet.getString("reviews").split(",")).mapToInt(Integer::parseInt).toArray();
            for (int reviewId : reviewIds) {
                FoodDeliveryService.getReviewDao().get(reviewId).ifPresent(reviews::add);
            }
        }

        Queue<Order> queue = new LinkedList<>();
        if(!resultSet.getString("queue").isEmpty()) {
            int[] queueIDs = Arrays.stream(resultSet.getString("queue").split(",")).mapToInt(Integer::parseInt).toArray();
            for (int queueID : queueIDs) {
                FoodDeliveryService.getOrderRepositoryService().get(queueID).ifPresent(queue::add);
            }
        }
        double rating = reviewsToRating(reviews);
        return new Restaurant(id, name, menuItems, reviews, queue, rating);
    }

    private String joinMenuItemIds(List<MenuItem> menuItems) {
        return menuItems.stream().map(mi -> Integer.toString(mi.getId())).collect(Collectors.joining(","));
    }

    private String joinReviewIds(List<Review> reviews) {
        return reviews.stream().map(r -> Integer.toString(r.getId())).collect(Collectors.joining(","));
    }

    private String joinQueueIds(Queue<Order> queue) {
        return queue.stream().map(o -> Integer.toString(o.getId())).collect(Collectors.joining(","));
    }
}
