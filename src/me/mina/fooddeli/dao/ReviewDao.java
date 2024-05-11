package me.mina.fooddeli.dao;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.mina.fooddeli.utils.Constants.REVIEWS_TABLE;

public class ReviewDao implements Dao<Review> {

    public ReviewDao() {
        createIfNotExists();
    }

    private void createIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + REVIEWS_TABLE + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT," +
                "text VARCHAR(255)," +
                "rating INT" +
                ");";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try(Statement statement = connection.createStatement()){
            statement.execute(sql);
            System.out.println("Checked/created the table 'reviews' successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Review> get(int id) {
        String sql = "SELECT * FROM " + REVIEWS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createReviewFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM " + REVIEWS_TABLE;
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reviews.add(createReviewFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public int create(Review review) {
        String sql = "INSERT INTO " + REVIEWS_TABLE + " (user_id, text, rating) VALUES (?, ?, ?)";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, review.getUser().getId());
            statement.setString(2, review.getText());
            statement.setInt(3, review.getRating());
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
    public void update(int id, Review review) {
        String sql = "UPDATE " + REVIEWS_TABLE + " SET user_id = ?, text = ?, rating = ? WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, review.getUser().getId());
            statement.setString(2, review.getText());
            statement.setInt(3, review.getRating());
            statement.setInt(4, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update error: The review with id " + id + " does not exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Review review) {
        String sql = "DELETE FROM " + REVIEWS_TABLE + " WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, review.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Review createReviewFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String text = resultSet.getString("text");
        int rating = resultSet.getInt("rating");
        int userId = resultSet.getInt("user_id");
        return new Review(id, FoodDeliveryService.getUserRepositoryService().get(userId).orElse(null), text, rating);
    }
}
