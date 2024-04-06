package me.mina.fooddeli.model;

public interface Reviewable {
    void addReview(Review review);
    void removeReview(Review review);
    double getAverageRating();
}