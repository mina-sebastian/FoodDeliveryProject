package me.mina.fooddeli.dao;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.person.DeliveryPerson;
import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.mina.fooddeli.utils.Constants.DELIVERY_PERSONS;

public class DeliveryPersonDao implements Dao<DeliveryPerson> {

    private void createIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + DELIVERY_PERSONS + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "current_order_id INT," +
                "reviews TEXT" +
                ");";

        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (Statement statement = connection.createStatement()){
            statement.execute(sql);
            System.out.println("Checked/created the table 'delivery_persons' successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DeliveryPersonDao() {
        createIfNotExists();
    }

    @Override
    public Optional<DeliveryPerson> get(int id) {
        String sql = "SELECT * FROM "+DELIVERY_PERSONS+" WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createDeliveryPersonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<DeliveryPerson> getAll() {
        List<DeliveryPerson> deliveryPeople = new ArrayList<>();
        String sql = "SELECT * FROM " + DELIVERY_PERSONS;
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deliveryPeople.add(createDeliveryPersonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveryPeople;
    }

    @Override
    public int create(DeliveryPerson obj) {
        String sql = "INSERT INTO " + DELIVERY_PERSONS + " (name, password, current_order_id, reviews) VALUES (?, ?, ?, ?)";
        for(int i = 0; i < obj.getReviews().size(); i++) {
            Review review = obj.getReviews().get(i);
            if(review.getId() < 1 || FoodDeliveryService.getReviewDao().get(review.getId()).isEmpty()) {
                int reviewId = FoodDeliveryService.getReviewDao().create(review);
                Optional<Review> newOptReview = FoodDeliveryService.getReviewDao().get(reviewId);
                if(newOptReview.isPresent())
                    obj.getReviews().set(i, newOptReview.get());
            }
        }
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getPassword());
            statement.setInt(3, obj.getCurrentOrder() != null ? obj.getCurrentOrder().getId() : -1);
            statement.setString(4, joinReviewIds(obj.getReviews()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void update(int id, DeliveryPerson obj) {
        String sql = "UPDATE " + DELIVERY_PERSONS + " SET name = ?, password = ?, current_order_id = ?, reviews = ? WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getPassword());
            statement.setInt(3, obj.getCurrentOrder() != null ? obj.getCurrentOrder().getId() : -1);
            statement.setString(4, joinReviewIds(obj.getReviews()));
            statement.setInt(5, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update error: DeliveryPerson with id " + id + " not found");
            }
            for(Review review : obj.getReviews()) {
                if(review.getId() < 1 || FoodDeliveryService.getReviewDao().get(review.getId()).isEmpty()) {
                    FoodDeliveryService.getReviewDao().create(review);
                } else {
                    FoodDeliveryService.getReviewDao().update(review.getId(), review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(DeliveryPerson obj) {
        String sql = "DELETE FROM " + DELIVERY_PERSONS + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, obj.getId());
            statement.executeUpdate();
            for (Review review : obj.getReviews()) {
                FoodDeliveryService.getReviewDao().delete(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DeliveryPerson createDeliveryPersonFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        Order order = FoodDeliveryService.getOrderRepositoryService()
                        .get(resultSet.getInt("current_order_id"))
                        .orElse(null);
        List<Review> reviews = new ArrayList<>();
        int[] reviewIds = Arrays.stream(resultSet.getString("reviews").split(",")).mapToInt(Integer::parseInt).toArray();
        for (int reviewId : reviewIds) {
            FoodDeliveryService.getReviewDao().get(reviewId).ifPresent(reviews::add);
        }
        return new DeliveryPerson(id, resultSet.getString("name"), resultSet.getString("password"), order, reviews);
    }

    private String joinReviewIds(List<Review> reviews) {
        return reviews.stream().map(r -> Integer.toString(r.getId())).collect(Collectors.joining(","));
    }
}
