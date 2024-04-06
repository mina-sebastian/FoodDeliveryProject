package me.mina.fooddeli.model.review;

import java.util.List;

public interface Reviewable {
    void addReview(Review review);
    void removeReview(Review review);

    List<Review> getReviews();

    double getAverageRating();
}